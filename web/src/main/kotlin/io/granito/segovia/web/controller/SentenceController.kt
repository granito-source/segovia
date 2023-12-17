package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import io.granito.segovia.web.model.SentenceNotFoundException
import io.granito.segovia.web.model.SentenceResource
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
    fun get(): Mono<CollectionModel<SentenceResource>> =
        try {
            searchSentencesCase.search()
                .map { SentenceResource(it) }
                .collectList()
                .map {
                    CollectionModel.of(it)
                        .withFallbackType(SentenceResource::class.java)
                        .add(Link.of(SENTENCES))
                }
        } catch (ex: Exception) {
            Mono.error(ex)
        }

    @GetMapping(SENTENCE)
    fun getOne(@PathVariable("id") id: String): Mono<SentenceResource> =
        try {
            fetchSentenceCase.fetch(id)
                .map { SentenceResource(it) }
                .switchIfEmpty(Mono.error(SentenceNotFoundException(id)))
        } catch (ex: Exception) {
            Mono.error(ex)
        }
}
