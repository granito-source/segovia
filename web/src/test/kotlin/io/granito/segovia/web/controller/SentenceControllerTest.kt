package io.granito.segovia.web.controller

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import io.granito.segovia.web.NotFoundException
import io.granito.segovia.web.model.SentenceModel
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.junit.jupiter.MockitoSettings
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

        doReturn(sentences).`when`(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.resolvableType.resolveGeneric())
                    .isEqualTo(SentenceModel::class.java)
                assertThat(it.content).isEmpty()
            }
            .verifyComplete()
    }

    @Test
    fun `get() emits collection of models when search() emits sentences`() {
        val sentences = createCold<Sentence>()
            .emit(
                Sentence("deadbeef", "One."),
                Sentence("babefeed", "Two.")
            )
            .flux()

        doReturn(sentences).`when`(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .assertNext {
                assertThat(it.resolvableType.resolveGeneric())
                    .isEqualTo(SentenceModel::class.java)
                assertThat(it.content)
                    .extracting(SentenceModel::id, SentenceModel::text)
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
            .emit(Sentence("deadbeef", "Llueve."))
            .flux()

        doReturn(sentences).`when`(searchSentencesCase).search()

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
            .next(Sentence("deadbeef", "Se acabo."))
            .error(t)
            .flux()

        doReturn(error).`when`(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `get() wraps exception, when search() fails`() {
        val t = RuntimeException("sentences")

        doThrow(t).`when`(searchSentencesCase).search()

        StepVerifier.create(controller.get())
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `getOne() emits error when it does not exist`() {
        val sentence = createCold<String>()
            .complete()
            .mono()

        doReturn(sentence).`when`(fetchSentenceCase).fetch("deadbeef")

        StepVerifier.create(controller.getOne("deadbeef"))
            .verifyErrorSatisfies {
                assertThat(it)
                    .isInstanceOf(NotFoundException::class.java)
                    .hasMessage("sentence with ID 'deadbeef' is not found")
            }
    }

    @Test
    fun `getOne() emits sentence when it exists`() {
        val sentence = createCold<String>()
            .emit("Case returns something.")
            .mono()

        doReturn(sentence).`when`(fetchSentenceCase).fetch("deadbeef")

        StepVerifier.create(controller.getOne("deadbeef"))
            .assertNext {
                assertThat(it.id).isEqualTo("deadbeef")
                assertThat(it.text).isEqualTo("Case returns something.")
            }
            .verifyComplete()
    }

    @Test
    fun `getOne() adds self link when it returns the sentence`() {
        val sentence = createCold<String>()
            .emit("Case returns something.")
            .mono()

        doReturn(sentence).`when`(fetchSentenceCase).fetch("deadbeef")

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
        val error = createCold<String>()
            .error(t)
            .mono()

        doReturn(error).`when`(fetchSentenceCase).fetch("deadbeef")

        StepVerifier.create(controller.getOne("deadbeef"))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }

    @Test
    fun `getOne() wraps exception, when fetch() fails`() {
        val t = RuntimeException("sentence")

        doThrow(t).`when`(fetchSentenceCase).fetch("deadbeef")

        StepVerifier.create(controller.getOne("deadbeef"))
            .verifyErrorSatisfies {
                assertThat(it).isSameAs(t)
            }
    }
}
