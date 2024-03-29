package io.granito.segovia.core.service

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import io.granito.segovia.core.repo.SentenceRepo
import io.granito.segovia.core.usecase.CreateSentenceCase
import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.core.usecase.SearchSentencesCase
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class SentenceService(private val sentenceRepo: SentenceRepo):
    SearchSentencesCase, FetchSentenceCase, CreateSentenceCase {
    override fun search(): Flux<Sentence> =
        try {
            sentenceRepo.select()
        } catch (ex: Exception) {
            Flux.error(ex)
        }

    override fun fetch(id: Slug): Mono<Sentence> =
        try {
            sentenceRepo.load(id)
        } catch (ex: Exception) {
            Mono.error(ex)
        }

    override fun create(text: String): Mono<Sentence> =
        try {
            val sentence = Sentence(text)

            sentenceRepo.insert(sentence).thenReturn(sentence)
        } catch (ex: Exception) {
            Mono.error(ex)
        }
}
