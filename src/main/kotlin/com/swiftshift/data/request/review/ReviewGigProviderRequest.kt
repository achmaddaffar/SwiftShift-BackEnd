package com.swiftshift.data.request.review

data class ReviewGigProviderRequest(
    val gigProviderId: String,
    val review: String,
    val star: Int
)
