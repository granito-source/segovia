package io.granito.segovia.web.controller

import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.model.langFrom
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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

const val LANGUAGES = "$ROOT/languages"
const val LANGUAGE = "$LANGUAGES/{lang}"
const val SENTENCES = "$LANGUAGE/sentences"
const val SENTENCE = "$SENTENCES/{id}"

@RestController
@RequestMapping(produces = [MediaTypes.HAL_JSON_VALUE])
class SentenceController(
    private val searchSentencesCase: SearchSentencesCase,
    private val fetchSentenceCase: FetchSentenceCase) {
    @GetMapping(SENTENCES)
    fun get(@PathVariable("lang") code: String):
        Mono<CollectionModel<SentenceResource>> {
        val lang = langFrom(code)

        return Flux.defer { searchSentencesCase.search(langFrom(code)) }
            .map { SentenceResource(it) }
            .collectList()
            .map {
                CollectionModel.of(it)
                    .withFallbackType(SentenceResource::class.java)
                    .add(Link.of(SENTENCES).expand(lang))
                }
    }

    @GetMapping(SENTENCE)
    fun getOne(@PathVariable("lang") code: String,
        @PathVariable("id") id: String): Mono<SentenceResource> = Mono
        .defer { fetchSentenceCase.fetch(langFrom(code), Slug(id)) }
        .map { SentenceResource(it) }
        .switchIfEmpty(Mono.error(SentenceNotFoundException(id)))
}
