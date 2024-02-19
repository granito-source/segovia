package io.granito.segovia.web.controller

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import io.granito.segovia.web.model.SentenceNotFoundException
import io.granito.segovia.web.model.SentenceResource
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.mockito.quality.Strictness
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher.createCold

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class SentenceControllerTest {
    @Mock
    private lateinit var searchSentencesCase: SearchSentencesCase

    @Mock
    private lateinit var fetchSentenceCase: FetchSentenceCase

    @InjectMocks
    private lateinit var controller: SentenceController

    @Test
    fun `constructor creates controller normally`() {
        assertThat(controller).isNotNull
    }

    @Test
    fun `get() emits empty collection when search() emits nothing`() {
        val sentences = createCold<Sentence>()
            .complete()
            .flux()

        doReturn(sentences).whenever(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.resolvableType.resolveGeneric())
                    .isEqualTo(SentenceResource::class.java)
                assertThat(it.content).isEmpty()
            }
            .verifyComplete()
    }

    @Test
    fun `get() emits collection of resources when search() emits sentences`() {
        val sentences = createCold<Sentence>()
            .emit(
                Sentence(Slug("deadbeef"), "One."),
                Sentence(Slug("babefeed"), "Two.")
            )
            .flux()

        doReturn(sentences).whenever(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.resolvableType.resolveGeneric())
                    .isEqualTo(SentenceResource::class.java)
                assertThat(it.content)
                    .extracting(SentenceResource::id, SentenceResource::text)
                    .containsExactly(
                        tuple("deadbeef", "One."),
                        tuple("babefeed", "Two.")
                    )
            }
            .verifyComplete()
    }

    @Test
    fun `get() adds self link to collection`() {
        val sentences = createCold<Sentence>()
            .emit(Sentence("Llueve."))
            .flux()

        doReturn(sentences).whenever(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.getRequiredLink("self").href)
                    .isEqualTo("/api/v1/sentences")
            }
            .verifyComplete()
    }

    @Test
    fun `get() propagates error, when search() emits error`() {
        val t = RuntimeException("sentences")
        val error = createCold<Sentence>()
            .next(Sentence("Se acabo."))
            .error(t)
            .flux()

        doReturn(error).whenever(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `get() wraps exception, when search() fails`() {
        val t = RuntimeException("sentences")

        doThrow(t).whenever(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `getOne() emits error when it does not exist`() {
        val sentence = createCold<Sentence>()
            .complete()
            .mono()

        doReturn(sentence).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        StepVerifier.create(controller.getOne("deadbeef"))
            .verifyErrorSatisfies {
                assertThat(it)
                    .isInstanceOf(SentenceNotFoundException::class.java)
                    .hasMessageContaining("'deadbeef' is not found")
            }
    }

    @Test
    fun `getOne() emits sentence when it exists`() {
        val sentence = createCold<Sentence>()
            .emit(Sentence(Slug("deadbeef"), "Get it."))
            .mono()

        doReturn(sentence).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        StepVerifier.create(controller.getOne("deadbeef"))
            .assertNext {
                assertThat(it.id).isEqualTo("deadbeef")
                assertThat(it.text).isEqualTo("Get it.")
            }
            .verifyComplete()
    }

    @Test
    fun `getOne() adds self link when it returns the sentence`() {
        val sentence = createCold<Sentence>()
            .emit(Sentence(Slug("deadbeef"), "Get it."))
            .mono()

        doReturn(sentence).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        StepVerifier.create(controller.getOne("deadbeef"))
            .assertNext {
                assertThat(it.getRequiredLink("self").href)
                    .isEqualTo("/api/v1/sentences/deadbeef")
            }
            .verifyComplete()
    }

    @Test
    fun `getOne() propagates error, when fetch() emits error`() {
        val t = RuntimeException("sentence")
        val error = createCold<Sentence>()
            .error(t)
            .mono()

        doReturn(error).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        StepVerifier.create(controller.getOne("deadbeef"))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `getOne() wraps exception, when fetch() fails`() {
        val t = RuntimeException("sentence")

        doThrow(t).whenever(fetchSentenceCase).fetch(Slug("deadbeef"))

        StepVerifier.create(controller.getOne("deadbeef"))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }
}
