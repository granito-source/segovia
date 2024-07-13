package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.GetStatusCase
import io.granito.segovia.web.model.RootResource
import org.springframework.context.annotation.Lazy
import org.springframework.hateoas.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

const val ROOT = "/api/v1"

@RestController
@Lazy
@RequestMapping(ROOT, produces = [MediaTypes.HAL_JSON_VALUE])
class RootController(private val getStatusCase: GetStatusCase) {
    @GetMapping
    fun get(): Mono<RootResource> =
        try {
            getStatusCase.getStatus()
                .map { RootResource(it) }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
}
