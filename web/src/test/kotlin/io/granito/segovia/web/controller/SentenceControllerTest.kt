package io.granito.segovia.web.controller

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.publisher.TestPublisher.createCold
import java.time.Instant

@WebFluxTest(SentenceController::class)
class SentenceControllerTest {
    @MockBean
    private lateinit var searchSentencesCase: SearchSentencesCase

    @MockBean
    private lateinit var fetchSentenceCase: FetchSentenceCase

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun `returns resource with no sentences when search emits nothing`() {
        doReturn(createCold<Sentence>()
            .complete()
            .flux()
        ).whenever(searchSentencesCase).search()

        client.get()
            .uri("/api/v1/sentences")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": { "href": "/api/v1/sentences" }
                  }
                }
                """.trimIndent(), true)
    }

    @Test
    fun `returns resource with sentences when search emits sentences`() {
        doReturn(createCold<Sentence>()
            .emit(
                Sentence(Slug("deadbeef"), "One."),
                Sentence(Slug("babefeed"), "Two.")
            )
            .flux()
        ).whenever(searchSentencesCase).search()

        client.get()
            .uri("/api/v1/sentences")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": { "href": "/api/v1/sentences" }
                  },
                  "_embedded": {
                    "sentences": [
                      {
                        "_links": {
                          "self": { "href": "/api/v1/sentences/deadbeef" }
                        },
                        "id": "deadbeef",
                        "text": "One."
                      },
                      {
                        "_links": {
                          "self": { "href": "/api/v1/sentences/babefeed" }
                        },
                        "id": "babefeed",
                        "text": "Two."
                      }
                    ]
                  }
                }
                """.trimIndent(), true)
    }

    @Test
    fun `returns error when search emits error`() {
        doReturn(createCold<Sentence>()
            .next(Sentence("Se acabo."))
            .error(RuntimeException("sentences"))
            .flux()
        ).whenever(searchSentencesCase).search()

        val started = Instant.now()

        client.get()
            .uri("/api/v1/sentences")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1/sentences")
    }

    @Test
    fun `returns error when search throws exception`() {
        doThrow(RuntimeException("sentences"))
            .whenever(searchSentencesCase).search()

        val started = Instant.now()

        client.get()
            .uri("/api/v1/sentences")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1/sentences")
    }

    @Test
    fun `returns not found problem when sentence does not exist`() {
        doReturn(createCold<Sentence>()
            .complete()
            .mono()
        ).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        client.get()
            .uri("/api/v1/sentences/deadbeef")
            .exchange()
            .expectStatus().isNotFound
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .json(
                """
                {
                  "detail": "Sentence identified by 'deadbeef' is not found.",
                  "instance": "/api/v1/sentences/deadbeef",
                  "status": 404,
                  "title": "Sentence is not found.",
                  "type": "https://segovia.granito.io/problem/not-found/sentence"
                }
                """.trimIndent(), true)
    }

    @Test
    fun `returns sentence resource when it exists`() {
        doReturn(createCold<Sentence>()
            .emit(Sentence(Slug("deadbeef"), "Get it."))
            .mono()
        ).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        client.get()
            .uri("/api/v1/sentences/deadbeef")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": { "href": "/api/v1/sentences/deadbeef" }
                  },
                  "id": "deadbeef",
                  "text": "Get it."
                }
                """.trimIndent(), true)
    }

    @Test
    fun `returns error when fetching sentence emits error`() {
        doReturn(createCold<Sentence>()
            .error(RuntimeException("sentence"))
            .mono()
        ).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        val started = Instant.now()

        client.get()
            .uri("/api/v1/sentences/deadbeef")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1/sentences/deadbeef")
    }

    @Test
    fun `returns error when fetching sentence throws exception`() {
        doThrow(RuntimeException("sentence"))
            .whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        val started = Instant.now()

        client.get()
            .uri("/api/v1/sentences/deadbeef")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1/sentences/deadbeef")
    }
}
