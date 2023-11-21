package com.swiftshift.data.repository.applying

import com.swiftshift.data.model.Applying
import com.swiftshift.data.model.Gig
import com.swiftshift.data.model.GigWorker
import com.swiftshift.util.Constants
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ApplyingRepository(
    db: CoroutineDatabase
) : IApplyingRepository {

    private val applying = db.getCollection<Applying>()
    private val gigWorkers = db.getCollection<GigWorker>()
    private val gigs = db.getCollection<Gig>()

    override suspend fun applyGigIfExist(gigWorkerId: String, gigId: String): Boolean {
        val doesGigWorkerExist = gigWorkers.findOneById(gigWorkerId) != null
        val doesGigExist = gigs.findOneById(gigId) != null

        if (!doesGigWorkerExist || !doesGigExist)
            return false

        return applying.insertOne(
            Applying(
                gigWorkerId = gigWorkerId,
                gigId = gigId,
                status = Constants.GIG_STATUS_PENDING,
                timestamp = System.currentTimeMillis()
            )
        ).wasAcknowledged()
    }

    override suspend fun unapplyGigIfExist(gigWorkerId: String, gigId: String): Boolean {
        return applying.deleteOne(
            and(
                Applying::gigWorkerId eq gigWorkerId,
                Applying::gigId eq gigId
            )
        ).wasAcknowledged()
    }

    override suspend fun getAppliesByGigWorker(gigWorkerId: String): List<Applying> {
        return applying.find(
            Applying::gigWorkerId eq gigWorkerId
        ).toList()
    }

    override suspend fun doesGigWorkerApply(gigWorkerId: String, gigId: String): Boolean {
        return applying.findOne(
            Applying::gigWorkerId eq gigWorkerId,
            Applying::gigId eq gigId
        ) != null
    }

    override suspend fun updateApply(
        gigWorkerId: String,
        gigId: String,
        status: Int
    ): Boolean {
        val apply = applying.findOne(
            and(
                Applying::gigWorkerId eq gigWorkerId,
                Applying::gigId eq gigId
            )
        ) ?: return false

        return applying.updateOneById(
            id = gigWorkerId,
            update = Applying(
                gigWorkerId = apply.gigWorkerId,
                gigId = apply.gigId,
                status = status,
                timestamp = apply.timestamp,
                id = apply.id
            )
        ).wasAcknowledged()
    }
}