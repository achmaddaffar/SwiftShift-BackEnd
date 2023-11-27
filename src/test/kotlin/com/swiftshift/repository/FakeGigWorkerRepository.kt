package com.swiftshift.repository

import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.repository.gig_worker.IGigWorkerRepository
import com.swiftshift.data.request.gig_worker.UpdateGigWorkerRequest

class FakeGigWorkerRepository : IGigWorkerRepository {

    private val gigWorkers = mutableListOf<GigWorker>()

    override suspend fun createGigWorker(gigWorker: GigWorker): Boolean {
        gigWorkers.add(gigWorker)
        return gigWorkers.contains(gigWorker)
    }

    override suspend fun getGigWorkerById(id: String): GigWorker? {
        return gigWorkers.find { it.id == id }
    }

    override suspend fun getGigWorkerByEmail(email: String): GigWorker? {
        return gigWorkers.find { it.email == email }
    }

    override suspend fun updateGigWorker(
        gigWorkerId: String,
        profileImageUrl: String,
        request: UpdateGigWorkerRequest
    ): Boolean {
        val gigWorker = gigWorkers.find { it.id == gigWorkerId } ?: return false
        val updateGigWorker = GigWorker(
            fullName = request.fullName,
            email = request.email,
            password = request.email,
            timeStamp = gigWorker.timeStamp,
            profileImageUrl = profileImageUrl,
            totalIncome = request.totalIncome,
            gender = request.gender,
            highestEducation = request.highestEducation,
            id = gigWorkerId
        )
        return gigWorkers.add(updateGigWorker)
    }

    override suspend fun doesPasswordForGigWorkerMatch(email: String, enteredPassword: String): Boolean {
        val gigWorker = getGigWorkerByEmail(email) ?: return false
        return gigWorker.password == enteredPassword
    }

    override suspend fun doesEmailBelongToGigWorkerId(email: String, gigWorkerId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun searchForGigWorker(query: String): List<GigWorker> {
        TODO("Not yet implemented")
    }

    override suspend fun getGigWorkers(gigWorkerIds: List<String>): List<GigWorker> {
        TODO("Not yet implemented")
    }
}