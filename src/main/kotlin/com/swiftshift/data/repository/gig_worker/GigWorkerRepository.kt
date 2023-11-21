package com.swiftshift.data.repository.gig_worker

import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.request.gig_worker.UpdateGigWorkerRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.or
import org.litote.kmongo.regex

class GigWorkerRepository(
    db: CoroutineDatabase
) : IGigWorkerRepository {

    private val gigWorkers = db.getCollection<GigWorker>()

    override suspend fun createGigWorker(gigWorker: GigWorker): Boolean {
        return gigWorkers.insertOne(gigWorker).wasAcknowledged()
    }

    override suspend fun getGigWorkerById(id: String): GigWorker? {
        return gigWorkers.findOneById(id)
    }

    override suspend fun getGigWorkerByEmail(email: String): GigWorker? {
        return gigWorkers.findOne(GigWorker::email eq email)
    }

    override suspend fun updateGigWorker(
        gigWorkerId: String,
        profileImageUrl: String,
        request: UpdateGigWorkerRequest
    ): Boolean {
        val gigWorker = getGigWorkerById(gigWorkerId) ?: return false
        return gigWorkers.updateOneById(
            id = gigWorkerId,
            update = GigWorker(
                email = request.email,
                fullName = request.fullName,
                password = request.password,
                timeStamp = System.currentTimeMillis(),
                profileImageUrl = profileImageUrl,
                totalIncome = request.totalIncome,
                gender = request.gender,
                highestEducation = request.highestEducation,
                id = gigWorker.id
            )
        ).wasAcknowledged()
    }

    override suspend fun doesPasswordForGigWorkerMatch(email: String, enteredPassword: String): Boolean {
        val gigWorker = getGigWorkerByEmail(email)
        return gigWorker?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToGigWorkerId(email: String, gigWorkerId: String): Boolean {
        return gigWorkers.findOneById(gigWorkerId)?.email == email
    }

    override suspend fun searchForGigWorker(query: String): List<GigWorker> {
        return gigWorkers.find(
            or(
                GigWorker::fullName regex Regex("(?i).*$query.*"),
                GigWorker::email eq query
            )
        )
            .ascendingSort(GigWorker::fullName)
            .toList()
    }

    override suspend fun getGigWorkers(gigWorkerIds: List<String>): List<GigWorker> {
        return gigWorkers.find(GigWorker::id `in` gigWorkerIds).toList()
    }
}