package com.swiftshift.routes

import com.swiftshift.data.request.applying.ApplyGigRequest
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.service.ApplyingService
import com.swiftshift.service.GigService
import com.swiftshift.util.ApiResponseMessages
import com.swiftshift.util.gigWorkerId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.applyGig(
    applyingService: ApplyingService,
    gigService: GigService
) {
    authenticate {
        post("/api/applying/apply") {
            val request = runCatching { call.receiveNullable<ApplyGigRequest>() }.getOrNull() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val gig = gigService.getGigById(request.gigId) ?: run {
                call.respond(
                    HttpStatusCode.NotFound,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.GIG_NOT_FOUND
                    )
                )
                return@post
            }

            if (System.currentTimeMillis() > gig.deadlineDate) {
                call.respond(
                    HttpStatusCode.Locked,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.GIG_REGISTRATION_PASSED_DEADLINE
                    )
                )
                return@post
            }

            if (gig.currentApplier >= gig.maxApplier) {
                call.respond(
                    HttpStatusCode.Locked,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.GIG_REACHED_MAX_APPLIER
                    )
                )
                return@post
            }

            val acknowledged = applyingService.applyGigIfExist(
                call.gigWorkerId,
                request = request
            )
            if (acknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(successful = true)
                )
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}