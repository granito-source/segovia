package io.granito.segovia.core.service

import io.granito.segovia.core.model.Status
import io.granito.segovia.core.usecase.GetStatusCase

class StatusService(private val appVersion: String):
    GetStatusCase {
    override fun getStatus(): Status {
        return Status(appVersion, false)
    }
}
