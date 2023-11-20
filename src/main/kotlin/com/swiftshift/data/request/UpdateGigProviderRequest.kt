package com.swiftshift.data.request

data class UpdateGigProviderRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val profileImageUrl: String
)
