package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.repo.SentenceRepo
import io.granito.segovia.core.service.SentenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class FetchSentenceCaseTest {
    @Mock
    private lateinit var sentenceRepo: SentenceRepo

    @InjectMocks
    private lateinit var service: SentenceService

    private val id = Slug("deadbeef")

    private val result = TestPublisher.createCold<Sentence>()
        .complete()
        .mono()

    @Test
    fun `fetch() returns same data as repo when load succeeds`() {
        doReturn(result).whenever(sentenceRepo).load(id)

        assertThat(service.fetch(id)).isSameAs(result)
    }

    @Test
    fun `fetch() wraps exception when load fails`() {
        val t = RuntimeException("read")

        doThrow(t).whenever(sentenceRepo).load(any())

        StepVerifier.create(service.fetch(id))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }
}
