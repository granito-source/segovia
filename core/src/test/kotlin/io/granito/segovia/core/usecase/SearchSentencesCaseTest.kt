package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.repo.SentenceRepo
import io.granito.segovia.core.service.SentenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class SearchSentencesCaseTest {
    @Mock
    private lateinit var sentenceRepo: SentenceRepo

    @InjectMocks
    private lateinit var service: SentenceService

    private val result = TestPublisher.createCold<Sentence>()
        .complete()
        .flux()

    @Test
    fun `search() returns same data as repo when select succeeds`() {
        doReturn(result).whenever(sentenceRepo).select()

        assertThat(service.search(Lang.EN)).isSameAs(result)
    }

    @Test
    fun `search() wraps exception when select fails`() {
        val t = RuntimeException("select")

        doThrow(t).whenever(sentenceRepo).select()

        StepVerifier.create(service.search(Lang.ES))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }
}
