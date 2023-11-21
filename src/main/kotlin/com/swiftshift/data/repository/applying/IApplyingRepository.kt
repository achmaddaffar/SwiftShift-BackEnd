package com.swiftshift.data.repository.applying

import com.swiftshift.data.model.Applying

interface IApplyingRepository {

    suspend fun applyGigIfExist(
        gigWorkerId: String,
        gigId: String
    ): Boolean

    suspend fun unapplyGigIfExist(
        gigWorkerId: String,
        gigId: String
    ): Boolean

    suspend fun getAppliesByGigWorker(gigWorkerId: String): List<Applying>

    suspend fun doesGigWorkerApply(
        gigWorkerId: String,
        gigId: String
    ): Boolean

    suspend fun updateApply(applyId: String, status: Int): Boolean
}