package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.GetStatusCase
import io.granito.segovia.core.model.Status
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.hateoas.Link

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
internal class RootControllerTest {
    @Mock
    private lateinit var getStatusCase: GetStatusCase

    @InjectMocks
    private lateinit var controller: RootController

    @BeforeEach
    fun setUp() {
        lenient().doReturn(Status("2.3.7", false))
            .`when`(getStatusCase).getStatus()
    }

    @Test
    fun `constructor creates controller normally`() {
        assertThat(controller).isNotNull
    }

    @Test
    fun `get() returns uses appVersion as apiVersion normally`() {
        doReturn(Status("1.3.2", false)).`when`(getStatusCase).getStatus()

        assertThat(controller.get().apiVersion).isEqualTo("1.3.2")
    }

    @Test
    fun `get() returns UP status when not draining`() {
        assertThat(controller.get().status).isEqualTo("UP")
    }

    @Test
    fun `get() returns DRAIN status when draining`() {
        doReturn(Status("1.3.2", true)).`when`(getStatusCase).getStatus()

        assertThat(controller.get().status).isEqualTo("DRAIN")
    }

    @Test
    fun `get() sets self HAL link normally`() {
        assertThat(controller.get().getLink("self")).get()
            .isEqualTo(Link.of("/api/v1"))
    }

    @Test
    fun `get() propagates exception, when getStatus() fails`() {
        val t = RuntimeException("status")

        doThrow(t).`when`(getStatusCase).getStatus()

        assertThatThrownBy { controller.get() }.isSameAs(t)
    }
}
