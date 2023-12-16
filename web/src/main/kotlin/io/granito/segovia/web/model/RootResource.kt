package io.granito.segovia.web.model

import io.granito.segovia.core.model.Status
import io.granito.segovia.web.controller.ROOT
import io.granito.segovia.web.controller.SENTENCES
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(itemRelation = "root")
class RootResource(val status: String, val apiVersion: String):
    RepresentationModel<RootResource>() {
    constructor(status: Status):
        this(if (status.drain) "DRAIN" else "UP", status.appVersion)

    init {
        add(Link.of(ROOT), Link.of(SENTENCES, "sentences"))
    }
}
