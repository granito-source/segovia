package io.granito.segovia.web.controller

import java.time.Instant
import java.time.temporal.ChronoUnit
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.ResponseSpec.expect5xxError(from: Instant, to: Instant):
    WebTestClient.BodyContentSpec = expectStatus().is5xxServerError
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.status").isEqualTo(500)
        .jsonPath("$.error").isEqualTo("Internal Server Error")
        .jsonPath("$.timestamp").value<String> {
            assertThat(Instant.parse(it).truncatedTo(ChronoUnit.SECONDS))
                .isAfterOrEqualTo(from.truncatedTo(ChronoUnit.SECONDS))
                .isBeforeOrEqualTo(to.truncatedTo(ChronoUnit.SECONDS))
        }
        .jsonPath("$.requestId").isNotEmpty
