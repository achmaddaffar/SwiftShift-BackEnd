package com.swiftshift.data.request

import com.swiftshift.util.Constants.Empty

data class CreateGigProviderRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val profileImageUrl: String = String.Empty
)
