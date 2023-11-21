package com.swiftshift.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.swiftshift.util.Constants
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    authentication {
        jwt {
            val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            val issuer = this@configureSecurity.environment.config.property("jwt.domain").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience(jwtAudience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}

val JWTPrincipal.gigWorkerId: String?
    get() = getClaim(Constants.JWT_CLAIM_GIG_WORKER_ID, String::class)

val JWTPrincipal.gigProviderId: String?
    get() = getClaim(Constants.JWT_CLAIM_GIG_PROVIDER_ID, String::class)