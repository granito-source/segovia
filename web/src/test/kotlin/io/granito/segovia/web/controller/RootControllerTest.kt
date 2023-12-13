package io.granito.segovia.web.controller

import io.granito.segovia.core.model.Status
import io.granito.segovia.core.usecase.GetStatusCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher.createCold

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
internal class RootControllerTest {
    @Mock
    private lateinit var getStatusCase: GetStatusCase

    @InjectMocks
    private lateinit var controller: RootController

    @BeforeEach
    fun setUp() {
        val status = createCold<Status>()
            .emit(Status("2.3.7", false))
            .mono()

        lenient().doReturn(status).`when`(getStatusCase).getStatus()
    }

    @Test
    fun `constructor creates controller normally`() {
        assertThat(controller).isNotNull
    }

    @Test
    fun `get() uses appVersion as apiVersion normally`() {
        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.apiVersion).isEqualTo("2.3.7")
            }
            .verifyComplete()
    }

    @Test
    fun `get() returns UP status when not draining`() {
        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.status).isEqualTo("UP")
            }
            .verifyComplete()
    }

    @Test
    fun `get() returns DRAIN status when draining`() {
        val status = createCold<Status>()
            .emit(Status("1.2.3", true))
            .mono()

        doReturn(status).`when`(getStatusCase).getStatus()

        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.status).isEqualTo("DRAIN")
            }
            .verifyComplete()
    }

    @Test
    fun `get() sets self HAL link normally`() {
        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.getRequiredLink("self").href)
                    .isEqualTo("/api/v1")
            }
            .verifyComplete()
    }

    @Test
    fun `get() sets sentence HAL link normally`() {
        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.getRequiredLink("sentence").href)
                    .isEqualTo("/api/v1/sentences/default")
            }
            .verifyComplete()
    }

    @Test
    fun `get() propagates error, when getStatus() emits error`() {
        val t = RuntimeException("status")
        val status = createCold<Status>()
            .error(t)
            .mono()

        doReturn(status).`when`(getStatusCase).getStatus()

        StepVerifier.create(controller.get())
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `get() wraps exception, when getStatus() fails`() {
        val t = RuntimeException("status")

        doThrow(t).`when`(getStatusCase).getStatus()

        StepVerifier.create(controller.get())
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }
}
