package com.swiftshift.service

import com.swiftshift.data.model.Review
import com.swiftshift.data.repository.review.IReviewRepository
import com.swiftshift.data.request.review.CreateReviewGigProviderRequest
import com.swiftshift.util.Constants

class ReviewService(
    private val reviewRepository: IReviewRepository
) {

    suspend fun reviewGigProviderIfExist(
        gigWorkerId: String,
        request: CreateReviewGigProviderRequest
    ): Boolean {
        return reviewRepository.reviewGigProviderIfExist(
            gigWorkerId = gigWorkerId,
            request = request
        )
    }

    suspend fun deleteReviewGigProviderIfExist(
        reviewId: String
    ): Boolean {
        return reviewRepository.deleteReviewIfExist(reviewId)
    }

    suspend fun getReviewById(
        reviewId: String
    ): Review? {
        return reviewRepository.getReviewById(reviewId)
    }

    suspend fun getReviewsByGigProvider(
        gigProviderId: String,
        page: Int,
        pageSize: Int
    ): List<Review> {
        return reviewRepository.getReviewsByGigProvider(
            gigProviderId,
            page,
            pageSize
        )
    }

    fun validateReviewRequest(
        request: CreateReviewGigProviderRequest
    ): ValidationEvent {
        if (request.review.length > Constants.MAX_REVIEW_LENGTH)
            return ValidationEvent.ReviewTooLong
        return ValidationEvent.Success
    }

    sealed class ValidationEvent {
        data object ReviewTooLong : ValidationEvent()
        data object Success : ValidationEvent()
    }
}