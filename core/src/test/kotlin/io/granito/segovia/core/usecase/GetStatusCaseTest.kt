package io.granito.segovia.core.usecase

import io.granito.segovia.core.service.StatusService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GetStatusCaseTest {
    private val getStatusCase = StatusService("2.5.7")

    @Test
    fun `constructor creates service, normally`() {
        assertThat(getStatusCase).isNotNull
    }

    @Test
    fun `getStatus() returns status, always`() {
        val status = getStatusCase.getStatus()

        assertThat(status.appVersion).isEqualTo("2.5.7")
        assertThat(status.drain).isFalse
    }
}
