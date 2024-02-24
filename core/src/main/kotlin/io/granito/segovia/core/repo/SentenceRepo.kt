package io.granito.segovia.core.repo

import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SentenceRepo {
    fun load(lang: Lang, id: Slug): Mono<Sentence>

    fun select(lang: Lang): Flux<Sentence>

    fun insert(sentence: Sentence): Mono<Unit>

    fun clear(): Mono<Unit>
}
