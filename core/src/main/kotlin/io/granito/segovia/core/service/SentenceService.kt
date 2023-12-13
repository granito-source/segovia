package io.granito.segovia.core.service

import io.granito.segovia.core.usecase.FetchSentenceCase
import reactor.core.publisher.Mono

class SentenceService: FetchSentenceCase {
    override fun fetch(id: String): Mono<String> {
        return Mono.just("XXX: not implemented")
    }
}
