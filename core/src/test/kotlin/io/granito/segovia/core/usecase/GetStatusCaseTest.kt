package io.granito.segovia.core.usecase

import io.granito.segovia.core.service.StatusService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class GetStatusCaseTest {
    private val getStatusCase = StatusService("2.5.7")

    @Test
    fun `constructor creates service, normally`() {
        assertThat(getStatusCase).isNotNull
    }

    @Test
    fun `getStatus() returns status, always`() {
        StepVerifier.create(getStatusCase.getStatus())
            .assertNext {
                assertThat(it.appVersion).isEqualTo("2.5.7")
                assertThat(it.drain).isFalse
            }
            .verifyComplete()
    }
}
