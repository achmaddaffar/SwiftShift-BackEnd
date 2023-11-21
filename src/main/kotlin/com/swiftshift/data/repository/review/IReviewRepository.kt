package com.swiftshift.data.repository.review

import com.swiftshift.data.model.Review
import com.swiftshift.data.request.review.ReviewGigProviderRequest

interface IReviewRepository {

    suspend fun reviewGigProviderIfExist(
        gigWorkerId: String,
        gigProviderId: String,
        request: ReviewGigProviderRequest
    ): Boolean

    suspend fun deleteReviewIfExist(
        gigWorkerId: String,
        gigProviderId: String,
    ): Boolean

    suspend fun getReviewsByGigProvider(gigProviderId: String): List<Review>

    suspend fun doesGigWorkerReview(
        gigWorkerId: String,
        gigProviderId: String
    ): Boolean
}