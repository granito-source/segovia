package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.repo.SentenceRepo
import io.granito.segovia.core.service.SentenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class CreateSentenceCaseTest {
    @Mock
    private lateinit var sentenceRepo: SentenceRepo

    @InjectMocks
    private lateinit var service: SentenceService

    @Test
    fun `create() returns persisted sentence when insert succeeds`() {
        val slug = Slug("C5iJuRBVzt1h")
        val sentence = "Llueve mucho."
        val result = TestPublisher.createCold<Unit>()
            .complete()
            .mono()

        doReturn(result).whenever(sentenceRepo).insert(argThat {
            id == slug && text == sentence
        })

        StepVerifier.create(service.create(Lang.ES, "Llueve mucho."))
            .assertNext {
                assertThat(it.id).isEqualTo(slug)
                assertThat(it.text).isEqualTo(sentence)
            }
            .verifyComplete()
    }

    @Test
    fun `create() propagates error when insert emits error`() {
        val t = RuntimeException("insert")
        val result = TestPublisher.createCold<Unit>()
            .error(t)
            .mono()

        doReturn(result).whenever(sentenceRepo).insert(any())

        StepVerifier.create(service.create(Lang.ES, "Se acabo."))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `create() wraps exception when insert fails`() {
        val t = RuntimeException("insert")

        doThrow(t).whenever(sentenceRepo).insert(any())

        StepVerifier.create(service.create(Lang.ES, "Se acabo."))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }
}
