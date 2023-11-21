package com.swiftshift.data.repository.gig_worker

import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.request.gig_worker.UpdateGigWorkerRequest

interface IGigWorkerRepository {

    suspend fun createGigWorker(gigWorker: GigWorker): Boolean

    suspend fun getGigWorkerById(id: String): GigWorker?

    suspend fun getGigWorkerByEmail(email: String): GigWorker?

    suspend fun updateGigWorker(
        gigWorkerId: String,
        profileImageUrl: String,
        request: UpdateGigWorkerRequest
    ): Boolean

    suspend fun doesPasswordForGigWorkerMatch(email: String, enteredPassword: String): Boolean

    suspend fun doesEmailBelongToGigWorkerId(email: String, gigWorkerId: String): Boolean

    suspend fun searchForGigWorker(query: String): List<GigWorker>

    suspend fun getGigWorkers(gigWorkerIds: List<String>): List<GigWorker>
}