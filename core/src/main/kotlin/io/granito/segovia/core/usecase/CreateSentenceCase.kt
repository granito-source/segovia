package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Lang
import io.granito.segovia.core.model.Sentence
import reactor.core.publisher.Mono

interface CreateSentenceCase {
    fun create(lang: Lang, text: String): Mono<Sentence>
}
