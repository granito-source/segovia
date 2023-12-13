package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.GetStatusCase
import io.granito.segovia.web.model.StatusResource
import org.springframework.hateoas.Link
import org.springframework.hateoas.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

const val ROOT = "/api/v1"

@RestController
@RequestMapping(ROOT, produces = [MediaTypes.HAL_JSON_VALUE])
class RootController(private val getStatusCase: GetStatusCase) {
    @GetMapping
    fun get(): Mono<StatusResource> =
        try {
            getStatusCase.getStatus()
                .map {
                    StatusResource(if (it.drain) "DRAIN" else "UP",
                        it.appVersion)
                        .add(Link.of(ROOT))
                        .add(Link.of(SENTENCE, "sentence").expand("default"))
                }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
}
