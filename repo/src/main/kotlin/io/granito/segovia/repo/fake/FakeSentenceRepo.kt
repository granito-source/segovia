package io.granito.segovia.repo.fake

import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.repo.SentenceRepo
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.TreeMap

class FakeSentenceRepo: SentenceRepo {
    private val content = TreeMap<Lang, TreeMap<Slug, Sentence>>()

    override fun load(lang: Lang, id: Slug): Mono<Sentence> =
        Mono.justOrEmpty(content[lang]?.get(id))

    override fun select(lang: Lang): Flux<Sentence> =
        Flux.fromIterable((content[lang] ?: TreeMap()).values)

    override fun insert(sentence: Sentence): Mono<Unit> {
        content.computeIfAbsent(sentence.lang) {
            TreeMap<Slug, Sentence>()
        }[sentence.id] = sentence

        return Mono.empty()
    }

    override fun clear(): Mono<Unit> {
        content.clear()

        return Mono.empty()
    }
}
