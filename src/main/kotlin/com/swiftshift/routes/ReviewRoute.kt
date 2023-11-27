package com.swiftshift.routes

import com.swiftshift.data.request.review.CreateReviewGigProviderRequest
import com.swiftshift.data.request.review.DeleteReviewRequest
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.service.ReviewService
import com.swiftshift.util.ApiResponseMessages
import com.swiftshift.util.Constants
import com.swiftshift.util.QueryParams
import com.swiftshift.util.gigWorkerId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createReviewGigProvider(
    reviewService: ReviewService
) {
    authenticate {
        post("/api/review/create") {
            val request = runCatching { call.receiveNullable<CreateReviewGigProviderRequest>() }.getOrNull() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            when (reviewService.validateReviewRequest(request)) {
                ReviewService.ValidationEvent.ReviewTooLong -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.REVIEW_TOO_LONG
                        )
                    )
                    return@post
                }

                ReviewService.ValidationEvent.Success -> {
                    val acknowledged = reviewService.reviewGigProviderIfExist(
                        gigWorkerId = call.gigWorkerId,
                        request = request
                    )

                    if (acknowledged) {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse<Unit>(
                                successful = true,
                                message = ApiResponseMessages.REVIEW_CREATED_SUCCESSFULLY
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}

fun Route.deleteReviewGigProvider(
    reviewService: ReviewService
) {
    authenticate {
        delete("api/review/delete") {
            val request = runCatching { call.receiveNullable<DeleteReviewRequest>() }.getOrNull() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val review = reviewService.getReviewById(request.reviewId)

            if (review == null) {
                call.respond(
                    HttpStatusCode.NotFound
                )
                return@delete
            }

            val acknowledged = reviewService.deleteReviewGigProviderIfExist(request.reviewId)

            if (acknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false
                    )
                )
            }
        }
    }
}

fun Route.getReviewGigProvider(
    reviewService: ReviewService
) {
    authenticate {
        get("/api/review/get") {
            val gigProviderId = call.parameters[QueryParams.PARAM_GIG_PROVIDER_ID] ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull()
                ?: Constants.DEFAULT_REVIEWS_TO_GIG_PROVIDER_SIZE

            val reviews = reviewService.getReviewsByGigProvider(
                gigProviderId = gigProviderId,
                page = page,
                pageSize = pageSize
            )

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = reviews
                )
            )
        }
    }
}