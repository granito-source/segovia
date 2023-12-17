package io.granito.segovia.repo.fake

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.repo.SentenceRepo
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class FakeSentenceRepo: SentenceRepo {
    override fun load(id: Slug): Mono<Sentence> = Mono.empty()

    override fun select(): Flux<Sentence> = Flux.empty()

    override fun insert(sentence: Sentence): Mono<Unit> = Mono.empty()
}
