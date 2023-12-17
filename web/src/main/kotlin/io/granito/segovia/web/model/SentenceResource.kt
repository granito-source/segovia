package io.granito.segovia.web.model

import io.granito.segovia.core.model.Sentence
import io.granito.segovia.web.controller.SENTENCE
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(itemRelation = "sentence", collectionRelation = "sentences")
class SentenceResource(val id: String, val text: String):
    RepresentationModel<SentenceResource>() {
    constructor(sentence: Sentence):
        this(sentence.id.toString(), sentence.text)

    init {
        add(Link.of(SENTENCE).expand(id))
    }
}
