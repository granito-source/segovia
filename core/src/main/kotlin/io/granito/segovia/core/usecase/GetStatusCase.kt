package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Status
import reactor.core.publisher.Mono

interface GetStatusCase {
    fun getStatus(): Mono<Status>
}
