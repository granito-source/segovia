package io.granito.segovia.web.model

import io.granito.segovia.web.controller.SENTENCE
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(itemRelation = "sentence", collectionRelation = "sentences")
class SentenceModel(val id: String, val text: String):
    RepresentationModel<SentenceModel>() {
    init {
        add(Link.of(SENTENCE).expand(id))
    }
}
