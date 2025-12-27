package io.granito.segovia.web.controller

import java.time.Instant
import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.json.JsonCompareMode
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.publisher.TestPublisher.createCold

@WebFluxTest(SentenceController::class)
class SentenceControllerTest {
    @MockitoBean
    private lateinit var searchSentencesCase: SearchSentencesCase

    @MockitoBean
    private lateinit var fetchSentenceCase: FetchSentenceCase

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun `returns resource with no sentences when search emits nothing`() {
        doReturn(createCold<Sentence>()
            .complete()
            .flux()
        ).whenever(searchSentencesCase).search(Lang.ES)

        client.get()
            .uri("/api/v1/languages/es/sentences")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": { "href": "/api/v1/languages/es/sentences" }
                  }
                }
                """.trimIndent(), JsonCompareMode.STRICT)
    }

    @Test
    fun `returns resource with sentences when search emits sentences`() {
        doReturn(createCold<Sentence>()
            .emit(
                Sentence(Lang.EN, Slug("deadbeef"), "One."),
                Sentence(Lang.EN, Slug("babefeed"), "Two.")
            )
            .flux()
        ).whenever(searchSentencesCase).search(Lang.EN)

        client.get()
            .uri("/api/v1/languages/en/sentences")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": { "href": "/api/v1/languages/en/sentences" }
                  },
                  "_embedded": {
                    "sentences": [
                      {
                        "_links": {
                          "self": {
                            "href": "/api/v1/languages/en/sentences/deadbeef"
                          }
                        },
                        "id": "deadbeef",
                        "text": "One."
                      },
                      {
                        "_links": {
                          "self": {
                            "href": "/api/v1/languages/en/sentences/babefeed"
                          }
                        },
                        "id": "babefeed",
                        "text": "Two."
                      }
                    ]
                  }
                }
                """.trimIndent(), JsonCompareMode.STRICT)
    }

    @Test
    fun `returns error when search emits error`() {
        doReturn(createCold<Sentence>()
            .next(Sentence(Lang.ES, "Se acabo."))
            .error(RuntimeException("sentences"))
            .flux()
        ).whenever(searchSentencesCase).search(Lang.ES)

        val started = Instant.now()

        client.get()
            .uri("/api/v1/languages/es/sentences")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1/languages/es/sentences")
    }

    @Test
    fun `returns error when search throws exception`() {
        doThrow(RuntimeException("sentences"))
            .whenever(searchSentencesCase).search(Lang.FR)

        val started = Instant.now()

        client.get()
            .uri("/api/v1/languages/fr/sentences")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1/languages/fr/sentences")
    }

    @Test
    fun `returns not found problem when sentence does not exist`() {
        doReturn(createCold<Sentence>()
            .complete()
            .mono()
        ).whenever(fetchSentenceCase).fetch(Lang.IT, Slug("deadbeef"))

        client.get()
            .uri("/api/v1/languages/it/sentences/deadbeef")
            .exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .json(
                """
                {
                  "detail": "Sentence identified by 'deadbeef' is not found.",
                  "instance": "/api/v1/languages/it/sentences/deadbeef",
                  "status": 404,
                  "title": "Sentence is not found.",
                  "type": "https://segovia.granito.io/problem/not-found/sentence"
                }
                """.trimIndent(), JsonCompareMode.STRICT)
    }

    @Test
    fun `returns sentence resource when it exists`() {
        doReturn(createCold<Sentence>()
            .emit(Sentence(Lang.EN, Slug("deadbeef"), "Get it."))
            .mono()
        ).whenever(fetchSentenceCase).fetch(Lang.EN, Slug("deadbeef"))

        client.get()
            .uri("/api/v1/languages/en/sentences/deadbeef")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": {
                      "href": "/api/v1/languages/en/sentences/deadbeef"
                    }
                  },
                  "id": "deadbeef",
                  "text": "Get it."
                }
                """.trimIndent(), JsonCompareMode.STRICT)
    }

    @Test
    fun `returns error when fetching sentence emits error`() {
        doReturn(createCold<Sentence>()
            .error(RuntimeException("sentence"))
            .mono()
        ).whenever(fetchSentenceCase).fetch(any(), any())

        val started = Instant.now()

        client.get()
            .uri("/api/v1/languages/it/sentences/deadbeef")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path")
            .isEqualTo("/api/v1/languages/it/sentences/deadbeef")
    }

    @Test
    fun `returns error when fetching sentence throws exception`() {
        doThrow(RuntimeException("sentence"))
            .whenever(fetchSentenceCase).fetch(any(), any())

        val started = Instant.now()

        client.get()
            .uri("/api/v1/languages/it/sentences/deadbeef")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path")
            .isEqualTo("/api/v1/languages/it/sentences/deadbeef")
    }
}
