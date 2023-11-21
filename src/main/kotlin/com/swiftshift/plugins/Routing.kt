package com.swiftshift.plugins

import com.swiftshift.routes.createGigWorker
import com.swiftshift.routes.getGigWorkerProfile
import com.swiftshift.routes.loginGigWorker
import com.swiftshift.routes.updateGigWorkerProfile
import com.swiftshift.service.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val applyingService: ApplyingService by inject()
    val gigProviderService: GigProviderService by inject()
    val gigService: GigService by inject()
    val gigWorkerService: GigWorkerService by inject()
    val reviewService: ReviewService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {

        // Gig Worker
        createGigWorker(
            gigWorkerService = gigWorkerService,
            gigProviderService = gigProviderService
        )
        loginGigWorker(
            gigWorkerService = gigWorkerService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )
        getGigWorkerProfile(
            gigWorkerService = gigWorkerService
        )
        updateGigWorkerProfile(
            gigWorkerService = gigWorkerService
        )

        staticResources("", "static")
    }
}
