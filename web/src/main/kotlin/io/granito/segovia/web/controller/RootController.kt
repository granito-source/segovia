package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.GetStatusCase
import io.granito.segovia.web.model.StatusResource
import org.springframework.hateoas.Link
import org.springframework.hateoas.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val ROOT = "/api/v1"

@RestController
@RequestMapping(ROOT, produces = [MediaTypes.HAL_JSON_VALUE])
class RootController(private val getStatusCase: GetStatusCase) {
    @GetMapping
    fun get(): StatusResource {
        val status = getStatusCase.getStatus()

        return StatusResource(if (status.drain) "DRAIN" else "UP",
            status.appVersion)
            .add(Link.of(ROOT))
    }
}
