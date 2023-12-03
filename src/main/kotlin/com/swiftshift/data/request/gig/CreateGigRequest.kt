package com.swiftshift.data.request.gig

data class CreateGigRequest(
    val title: String,
    val description: String,
    val tag: String,
    val gigProviderId: String,
    val maxApplier: Int,
    val deadlineDate: Long,
    val salary: Double,
    val latitude: Double,
    val longitude: Double,
)
