package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Sentence
import reactor.core.publisher.Flux

interface SearchSentencesCase {
    fun search(lang: Lang): Flux<Sentence>
}
