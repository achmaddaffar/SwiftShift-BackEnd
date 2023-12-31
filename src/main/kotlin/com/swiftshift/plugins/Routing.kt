package com.swiftshift.plugins

import com.swiftshift.routes.*
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
        // Auth
        authenticate(gigWorkerService, gigProviderService)

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
        getGigWorkerProfileById(gigWorkerService)
        getGigWorkerProfileByEmail(gigWorkerService)
        updateGigWorkerProfile(
            gigWorkerService = gigWorkerService,
            gigProviderService = gigProviderService
        )

        // Gig Provider
        createGigProvider(
            gigWorkerService = gigWorkerService,
            gigProviderService = gigProviderService
        )
        loginGigProvider(
            gigProviderService = gigProviderService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )
        getGigProviderProfileById(gigProviderService)
        getGigProviderProfileByEmail(gigProviderService)

        // Gig
        createGig(gigService, gigProviderService)
        getNearbyGigs(gigService)
        getRecommendedGigs(gigService)
        searchGig(gigService)
        getGigById(gigService)

        // Review
        createReviewGigProvider(reviewService)
        deleteReviewGigProvider(reviewService)
        getReviewGigProvider(reviewService)

        // Applying
        applyGig(applyingService, gigService)

        staticResources("", "static")
    }
}
