package io.granito.segovia.core.service

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class SentenceService: SearchSentencesCase, FetchSentenceCase {
    override fun search(): Flux<Sentence> = Flux.empty()

    override fun fetch(id: String): Mono<Sentence> = Mono.empty()
}
