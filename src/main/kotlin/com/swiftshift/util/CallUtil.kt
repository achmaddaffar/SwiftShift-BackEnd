package com.swiftshift.util

import com.swiftshift.plugins.gigProviderId
import com.swiftshift.plugins.gigWorkerId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val ApplicationCall.gigWorkerId: String
    get() = principal<JWTPrincipal>()?.gigWorkerId.toString()

val ApplicationCall.gigProviderId: String
    get() = principal<JWTPrincipal>()?.gigProviderId.toString()