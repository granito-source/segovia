package io.granito.segovia.web.controller

import io.granito.segovia.core.usecase.FetchSentenceCase
import io.granito.segovia.web.model.SentenceModel
import org.springframework.hateoas.MediaTypes
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

const val SENTENCES = "$ROOT/sentences"
const val SENTENCE = "$SENTENCES/{id}"

@RestController
class SentenceController(private val fetchSentenceCase: FetchSentenceCase) {
    @GetMapping(SENTENCE, produces = [MediaTypes.HAL_JSON_VALUE])
    fun get(@PathVariable("id") id: String): Mono<SentenceModel> {
        return fetchSentenceCase.fetch(id)
            .map { SentenceModel(id, it) }
    }
}
