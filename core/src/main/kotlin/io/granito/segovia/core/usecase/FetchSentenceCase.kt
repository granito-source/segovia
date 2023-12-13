package io.granito.segovia.core.usecase

import reactor.core.publisher.Mono

interface FetchSentenceCase {
    fun fetch(id: String): Mono<String>
}
