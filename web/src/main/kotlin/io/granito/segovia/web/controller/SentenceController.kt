package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import io.granito.segovia.web.NotFoundException
import io.granito.segovia.web.model.SentenceModel
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

const val SENTENCES = "$ROOT/sentences"
const val SENTENCE = "$SENTENCES/{id}"

@RestController
@RequestMapping(produces = [MediaTypes.HAL_JSON_VALUE])
class SentenceController(
    private val searchSentencesCase: SearchSentencesCase,
    private val fetchSentenceCase: FetchSentenceCase) {
    @GetMapping(SENTENCES)
    fun get(): Mono<CollectionModel<SentenceModel>> =
        try {
            searchSentencesCase.search()
                .map { SentenceModel(it.id, it.text) }
                .collectList()
                .map {
                    CollectionModel.of(it)
                        .withFallbackType(SentenceModel::class.java)
                        .add(Link.of(SENTENCES))
                }
        } catch (ex: Exception) {
            Mono.error(ex)
        }

    @GetMapping(SENTENCE)
    fun getOne(@PathVariable("id") id: String): Mono<SentenceModel> =
        try {
            fetchSentenceCase.fetch(id)
                .map { SentenceModel(id, it) }
                .switchIfEmpty(Mono.error(
                    NotFoundException("sentence with ID '$id' is not found")))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
}
