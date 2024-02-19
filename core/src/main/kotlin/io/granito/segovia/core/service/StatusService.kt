package io.granito.segovia.core.service

import io.granito.segovia.core.model.Status
import io.granito.segovia.core.usecase.GetStatusCase
import reactor.core.publisher.Mono

class StatusService(private val appVersion: String): GetStatusCase {
    override fun getStatus(): Mono<Status> {
        return Mono.just(Status(appVersion, false))
    }
}
