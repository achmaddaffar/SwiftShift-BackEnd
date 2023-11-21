package com.swiftshift.data.repository.gig

import com.swiftshift.data.model.Gig
import com.swiftshift.data.model.GigProvider
import com.swiftshift.data.response.gig.GigResponse
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import kotlin.math.abs

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

    override suspend fun getNearbyGigs(
        latitude: Long,
        longitude: Long,
        page: Int,
        pageSize: Int
    ): List<GigResponse> {
        return gigs.find()
            .toList()
            .map {
                val distance = abs(latitude - it.latitude) + abs(longitude - it.longitude)
                GigResponse(
                    title = it.title,
                    imageUrl = it.imageUrl,
                    description = it.description,
                    tag = it.tag,
                    gigProviderId = it.gigProviderId,
                    gigProviderName = it.gigProviderName,
                    maxApplier = it.maxApplier,
                    currentApplier = it.currentApplier,
                    deadlineDate = it.deadlineDate,
                    salary = it.salary,
                    longitude = it.longitude,
                    latitude = it.latitude,
                    timestamp = it.timestamp,
                    distance = distance
                )
            }
            .sortedBy { it.distance }
            .drop(page * pageSize)
            .take(pageSize)
    }
}