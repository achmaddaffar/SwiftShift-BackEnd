package com.swiftshift.data.repository.review

import com.swiftshift.data.model.GigProvider
import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.model.Review
import com.swiftshift.data.request.review.ReviewGigProviderRequest
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ReviewRepository(
    db: CoroutineDatabase
): IReviewRepository {

    private val gigWorkers = db.getCollection<GigWorker>()
    private val gigProviders = db.getCollection<GigProvider>()
    private val reviews = db.getCollection<Review>()

    override suspend fun reviewGigProviderIfExist(
        gigWorkerId: String,
        gigProviderId: String,
        request: ReviewGigProviderRequest
    ): Boolean {
        val doesGigWorkerExist = gigWorkers.findOneById(gigWorkerId) != null
        val doesGigProviderExist = gigProviders.findOneById(gigProviderId) != null

        if (!doesGigWorkerExist || !doesGigProviderExist)
            return false

        return reviews.insertOne(
            Review(
                review = request.review,
                star = request.star,
                gigWorkerId = gigWorkerId,
                gigProviderId = gigProviderId,
                timestamp = System.currentTimeMillis()
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteReviewIfExist(gigWorkerId: String, gigProviderId: String): Boolean {
        return reviews.deleteOne(
            and(
                Review::gigWorkerId eq gigWorkerId,
                Review::gigProviderId eq gigProviderId
            )
        ).wasAcknowledged()
    }

    override suspend fun getReviewsByGigProvider(gigProviderId: String): List<Review> {
        return reviews.find(
            Review::gigProviderId eq gigProviderId
        ).toList()
    }

    override suspend fun doesGigWorkerReview(gigWorkerId: String, gigProviderId: String): Boolean {
        return reviews.findOne(
            and(
                Review::gigWorkerId eq gigWorkerId,
                Review::gigProviderId eq gigProviderId
            )
        ) != null
    }
}