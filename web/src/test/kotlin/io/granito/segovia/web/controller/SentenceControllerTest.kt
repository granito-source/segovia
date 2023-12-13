package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.FetchSentenceCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher.createCold

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class SentenceControllerTest {
    @Mock
    private lateinit var fetchSentenceCase: FetchSentenceCase

    @InjectMocks
    private lateinit var controller: SentenceController

    @Test
    fun `constructor creates controller normally`() {
        assertThat(controller).isNotNull
    }

    @Test
    fun `get() returns sentence when it exists`() {
        val sentence = createCold<String>()
            .emit("Controller returns something.")
            .mono()

        doReturn(sentence).`when`(fetchSentenceCase).fetch("deadbeef")

        StepVerifier.create(controller.get("deadbeef"))
            .assertNext {
                assertThat(it.id).isEqualTo("deadbeef")
                assertThat(it.text).isEqualTo("Controller returns something.")
            }
            .verifyComplete()
    }
}
