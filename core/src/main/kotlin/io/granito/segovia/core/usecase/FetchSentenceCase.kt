package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.core.model.Slug
import reactor.core.publisher.Mono

interface FetchSentenceCase {
    fun fetch(id: Slug): Mono<Sentence>
}
