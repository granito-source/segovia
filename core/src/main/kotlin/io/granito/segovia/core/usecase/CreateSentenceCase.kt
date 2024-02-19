package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Sentence
import reactor.core.publisher.Mono

interface CreateSentenceCase {
    fun create(text: String): Mono<Sentence>
}
