package com.swiftshift.data.request.gig_worker

import com.swiftshift.util.Constants.Empty

data class CreateGigWorkerRequest(
    val fullName: String,
    val email: String,
    val password: String
)