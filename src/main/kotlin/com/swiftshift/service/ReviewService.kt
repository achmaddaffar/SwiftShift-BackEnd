package com.swiftshift.service

import com.swiftshift.data.repository.review.IReviewRepository
import com.swiftshift.data.request.review.ReviewGigProviderRequest

class ReviewService(
    private val reviewRepository: IReviewRepository
) {

    suspend fun reviewGigProviderIfExist(
        gigWorkerId: String,
        request: ReviewGigProviderRequest
    ): Boolean {
        return reviewRepository.reviewGigProviderIfExist(
            gigWorkerId = gigWorkerId,
            request = request
        )
    }

    suspend fun deleteReviewGigProviderIfExist(
        gigWorkerId: String,
        gigProviderId: String
    ): Boolean {
        return reviewRepository.deleteReviewIfExist(
            gigWorkerId,
            gigProviderId = gigProviderId
        )
    }
}