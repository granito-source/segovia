package io.granito.segovia.core.usecase

import io.granito.segovia.core.model.Status

interface GetStatusCase {
    fun getStatus(): Status
}
