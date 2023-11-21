package com.swiftshift.service

import com.swiftshift.data.repository.applying.IApplyingRepository
import com.swiftshift.data.request.applying.ApplyGigRequest

class ApplyingService(
    private val applyingRepository: IApplyingRepository
) {

    suspend fun applyGigIfExist(
        gigWorkerId: String,
        request: ApplyGigRequest
    ): Boolean {
        return applyingRepository.applyGigIfExist(
            gigWorkerId = gigWorkerId,
            gigId = request.gigId
        )
    }

    suspend fun unapplyGigIfExist(
        gigWorkerId: String,
        request: ApplyGigRequest
    ): Boolean {
        return applyingRepository.unapplyGigIfExist(
            gigWorkerId = gigWorkerId,
            gigId = request.gigId
        )
    }
}