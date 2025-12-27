package io.granito.segovia.web.controller

import java.time.Instant
import io.granito.segovia.core.model.Status
import io.granito.segovia.core.usecase.GetStatusCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.hateoas.MediaTypes
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.publisher.TestPublisher.createCold

@WebFluxTest(RootController::class)
internal class RootControllerTest {
    @MockitoBean
    private lateinit var getStatusCase: GetStatusCase

    @Autowired
    private lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        doReturn(createCold<Status>()
            .emit(Status("2.3.7", false))
            .mono()
        ).whenever(getStatusCase).getStatus()
    }

    @Test
    fun `returns API Root resource normally`() {
        client.get()
            .uri("/api/v1")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaTypes.HAL_JSON)
            .expectBody()
            .json(
                """
                {
                  "_links": {
                    "self": { "href": "/api/v1" },
                    "sentences": { "href": "/api/v1/languages/es/sentences" }
                  },
                  "apiVersion": "2.3.7",
                  "status": "UP"
                }
                """.trimIndent())
    }

    @Test
    fun `returns DRAIN status when draining`() {
        doReturn(createCold<Status>()
            .emit(Status("2.3.7", true))
            .mono()
        ).whenever(getStatusCase).getStatus()

        client.get()
            .uri("/api/v1")
            .exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$.status").isEqualTo("DRAIN")
    }

    @Test
    fun `returns error when getting status emits error`() {
        doReturn(createCold<Status>()
            .error(RuntimeException("status"))
            .mono()
        ).whenever(getStatusCase).getStatus()

        val started = Instant.now()

        client.get()
            .uri("/api/v1")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1")

    }

    @Test
    fun `returns error when getting status throws exception`() {
        val t = RuntimeException("status")

        doThrow(t).whenever(getStatusCase).getStatus()

        val started = Instant.now()

        client.get()
            .uri("/api/v1")
            .exchange()
            .expect5xxError(started, Instant.now())
            .jsonPath("$.path").isEqualTo("/api/v1")
    }
}
