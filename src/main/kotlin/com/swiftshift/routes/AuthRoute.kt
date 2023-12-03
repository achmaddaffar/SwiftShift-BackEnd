package com.swiftshift.routes

import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.service.GigProviderService
import com.swiftshift.service.GigWorkerService
import com.swiftshift.util.Constants
import com.swiftshift.util.gigProviderId
import com.swiftshift.util.gigWorkerId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authenticate(
    gigWorkerService: GigWorkerService,
    gigProviderService: GigProviderService
) {
    authenticate {
        get("/api/authenticate") {
            if (gigWorkerService.getGigWorkerById(call.gigWorkerId) != null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = Constants.GIG_WORKER_ROLE
                    )
                )
                return@get
            }

            if (gigProviderService.getGigProviderById(call.gigProviderId) != null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = Constants.GIG_PROVIDER_ROLE
                    )
                )
                return@get
            }

            call.respond(HttpStatusCode.NotFound)
        }
    }
}