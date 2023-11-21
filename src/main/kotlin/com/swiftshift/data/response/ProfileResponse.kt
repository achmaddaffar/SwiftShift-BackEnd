package com.swiftshift.data.response

import com.swiftshift.util.Constants.Empty

data class ProfileResponse(
    val fullName: String,
    val profileImageUrl: String,
    val joiningDate: Long,
    val email: String,
    val totalIncome: Double? = null,
    val gender: String? = null,
    val highestEducation: String? = null,
    val cvUrl: String? = null
)
