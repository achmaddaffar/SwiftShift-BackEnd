package com.swiftshift.routes

import com.google.gson.Gson
import com.swiftshift.data.request.gig.CreateGigRequest
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.service.GigService
import com.swiftshift.util.Constants
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
    gigService: GigService
) {
    val gson by inject<Gson>()

    authenticate {
        post("/api/gig/create") {
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