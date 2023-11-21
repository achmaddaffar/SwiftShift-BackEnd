package com.swiftshift.data.request.gig_provider

import com.swiftshift.util.Constants.Empty

data class CreateGigProviderRequest(
    val fullName: String,
    val email: String,
    val password: String
)
