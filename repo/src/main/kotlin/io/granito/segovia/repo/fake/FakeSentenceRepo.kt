package io.granito.segovia.repo.fake

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.repo.SentenceRepo
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.TreeMap

class FakeSentenceRepo: SentenceRepo {
    private val content = TreeMap<Slug, Sentence>()

    override fun load(id: Slug): Mono<Sentence> =
        Mono.justOrEmpty(content[id])

    override fun select(): Flux<Sentence> =
        Flux.fromIterable(content.values)

    override fun insert(sentence: Sentence): Mono<Unit> {
        content[sentence.id] = sentence

        return Mono.empty()
    }

    override fun clear(): Mono<Unit> {
        content.clear()

        return Mono.empty()
    }
}
