package io.granito.segovia.web.model

import org.springframework.hateoas.RepresentationModel

class StatusResource(val status: String, val apiVersion: String):
    RepresentationModel<StatusResource>()
