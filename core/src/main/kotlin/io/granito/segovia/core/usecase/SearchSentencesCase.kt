package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Sentence
import reactor.core.publisher.Flux

interface SearchSentencesCase {
    fun search(): Flux<Sentence>
}
