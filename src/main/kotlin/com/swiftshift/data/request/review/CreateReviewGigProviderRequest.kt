package com.swiftshift.data.request.review

data class CreateReviewGigProviderRequest(
    val gigProviderId: String,
    val review: String,
    val star: Int
)
