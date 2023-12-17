package io.granito.segovia.core.repo

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SentenceRepo {
    fun load(id: Slug): Mono<Sentence>

    fun select(): Flux<Sentence>

    fun insert(sentence: Sentence): Mono<Unit>
}
