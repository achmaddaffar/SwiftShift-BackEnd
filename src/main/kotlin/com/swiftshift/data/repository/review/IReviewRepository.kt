package com.swiftshift.data.repository.review

import com.swiftshift.data.model.Review
import com.swiftshift.data.request.review.CreateReviewGigProviderRequest
import com.swiftshift.util.Constants

interface IReviewRepository {

    suspend fun reviewGigProviderIfExist(
        gigWorkerId: String,
        request: CreateReviewGigProviderRequest
    ): Boolean

    suspend fun deleteReviewIfExist(
        reviewId: String
    ): Boolean

    suspend fun getReviewById(reviewId: String): Review?

    suspend fun getReviewsByGigProvider(
        gigProviderId: String,
        page: Int,
        pageSize: Int = Constants.DEFAULT_REVIEWS_TO_GIG_PROVIDER_SIZE
    ): List<Review>

    suspend fun doesGigWorkerReview(
        gigWorkerId: String,
        gigProviderId: String
    ): Boolean
}