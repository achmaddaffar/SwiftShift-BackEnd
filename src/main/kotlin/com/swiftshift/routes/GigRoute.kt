package com.swiftshift.routes

import com.google.gson.Gson
import com.swiftshift.data.model.Gig
import com.swiftshift.data.request.gig.CreateGigRequest
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.service.GigProviderService
import com.swiftshift.service.GigService
import com.swiftshift.util.Constants
import com.swiftshift.util.Constants.Empty
import com.swiftshift.util.QueryParams
import com.swiftshift.util.gigProviderId
import com.swiftshift.util.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.createGig(
    gigService: GigService,
    gigProviderService: GigProviderService
) {
    val gson by inject<Gson>()

    authenticate {
        post("/api/gig/create") {
            if (gigProviderService.getGigProviderById(call.gigProviderId) == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val multipart = call.receiveMultipart()
            var createGigRequest: CreateGigRequest? = null
            var fileName: String? = null
            var filePath: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FileItem -> {
                        fileName = partData.save(Constants.GIG_IMAGE_PATH)
                        filePath = "${Constants.GIG_IMAGE_PATH}$fileName"
                    }

                    is PartData.FormItem -> {
                        if (partData.name == Constants.CREATE_GIG_PART_DATA) {
                            createGigRequest = gson.fromJson(
                                partData.value,
                                CreateGigRequest::class.java
                            )
                        }
                    }

                    else -> Unit
                }
                partData.dispose()
            }

            createGigRequest?.let { request ->
                val gigImageUrl = "${Constants.BASE_URL}gig_images/$fileName"
                val createGigAcknowledged = gigService.createGig(
                    request = request,
                    imageUrl = gigImageUrl,
                    gigProviderId = call.gigProviderId
                )
                if (createGigAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    filePath?.let { File(it).delete() }
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: run {
                filePath?.let { File(it).delete() }
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.getNearbyGigs(
    gigService: GigService
) {
    authenticate {
        get("/api/gig/get_nearby") {
            val latitude = call.parameters[QueryParams.PARAM_LATITUDE]?.toDoubleOrNull()
            val longitude = call.parameters[QueryParams.PARAM_LONGITUDE]?.toDoubleOrNull()

            if (latitude == null || longitude == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_NEARBY_GIGS_PAGE_SIZE

            val nearbyGigs = gigService.getNearbyGigs(
                latitude = latitude,
                longitude = longitude,
                page = page,
                pageSize = pageSize
            )

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = nearbyGigs
                )
            )
        }
    }
}

fun Route.getRecommendedGigs(
    gigService: GigService
) {
    authenticate {
        get("/api/gig/get_recommended") {
            val latitude = call.parameters[QueryParams.PARAM_LATITUDE]?.toDoubleOrNull()
            val longitude = call.parameters[QueryParams.PARAM_LONGITUDE]?.toDoubleOrNull()

            if (latitude == null || longitude == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val recommendedGigs = gigService.getRecommendedGigs(
                latitude = latitude,
                longitude = longitude
            )

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = recommendedGigs
                )
            )
        }
    }
}

fun Route.searchGig(
    gigService: GigService
) {
    authenticate {
        get("/api/gig/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY] ?: run {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = emptyList<Gig>()
                    )
                )
                return@get
            }

            val gigs = gigService.searchGig(query)

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = gigs
                )
            )
        }
    }
}