package com.swiftshift.data.response.gig_worker

data class GigWorkerProfileResponse(
    val fullName: String,
    val profileImageUrl: String,
    val joiningDate: Long,
    val email: String,
    val totalIncome: Double? = null,
    val gender: String? = null,
    val highestEducation: String? = null,
    val cvUrl: String? = null
)
