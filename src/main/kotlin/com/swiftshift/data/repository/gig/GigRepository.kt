package com.swiftshift.data.repository.gig

import com.swiftshift.data.model.Gig
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class GigRepository(
    db: CoroutineDatabase
) : IGigRepository {

    private val gigs = db.getCollection<Gig>()

    override suspend fun createGig(gig: Gig): Boolean {
        return gigs.insertOne(gig).wasAcknowledged()
    }

    override suspend fun deleteGig(gigId: String): Boolean {
        return gigs.deleteOneById(gigId).wasAcknowledged()
    }

    override suspend fun getGigById(gigId: String): Gig? {
        return gigs.findOneById(gigId)
    }

    override suspend fun getGigsByGigProvider(gigProviderId: String, page: Int, pageSize: Int): List<Gig> {
        return gigs.find(
            Gig::gigProviderId eq gigProviderId
        )
            .ascendingSort(Gig::title)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
    }
}