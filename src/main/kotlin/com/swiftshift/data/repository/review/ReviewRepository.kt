package com.swiftshift.data.repository.review

import com.swiftshift.data.model.GigProvider
import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.model.Review
import com.swiftshift.data.request.review.CreateReviewGigProviderRequest
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
        request: CreateReviewGigProviderRequest
    ): Boolean {
        val doesGigWorkerExist = gigWorkers.findOneById(gigWorkerId) != null
        val doesGigProviderExist = gigProviders.findOneById(request.gigProviderId) != null

        if (!doesGigWorkerExist || !doesGigProviderExist)
            return false

        return reviews.insertOne(
            Review(
                review = request.review,
                star = request.star,
                gigWorkerId = gigWorkerId,
                gigProviderId = request.gigProviderId,
                timestamp = System.currentTimeMillis()
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteReviewIfExist(reviewId: String): Boolean {
        return reviews.deleteOne(Review::id eq reviewId).wasAcknowledged()
    }

    override suspend fun getReviewById(reviewId: String): Review? {
        return reviews.findOneById(reviewId)
    }

    override suspend fun getReviewsByGigProvider(
        gigProviderId: String,
        page: Int,
        pageSize: Int
    ): List<Review> {
        return reviews.find(
            Review::gigProviderId eq gigProviderId
        )
            .descendingSort(Review::timestamp)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
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