package com.swiftshift.data.request

data class UpdateGigWorkerRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val profileImageUrl: String
)
