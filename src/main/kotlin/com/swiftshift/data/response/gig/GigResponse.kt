package com.swiftshift.data.response.gig

data class GigResponse(
    val title: String,
    val imageUrl: String,
    val description: String,
    val tag: String,
    val gigProviderId: String,
    val gigProviderName: String,
    val maxApplier: Int,
    val currentApplier: Int,
    val deadlineDate: Long,
    val salary: Double,
    val longitude: Double,
    val latitude: Double,
    val timestamp: Long,
    val distance: Double,
    val id: String
)
