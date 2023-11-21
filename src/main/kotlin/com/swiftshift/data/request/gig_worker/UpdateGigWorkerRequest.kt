package com.swiftshift.data.request.gig_worker

data class UpdateGigWorkerRequest(
    val fullName: String,
    val email: String,
    val password: String
)
