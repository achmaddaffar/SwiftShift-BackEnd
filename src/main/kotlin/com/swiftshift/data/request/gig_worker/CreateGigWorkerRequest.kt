package com.swiftshift.data.request.gig_worker

data class CreateGigWorkerRequest(
    val fullName: String,
    val email: String,
    val password: String
)