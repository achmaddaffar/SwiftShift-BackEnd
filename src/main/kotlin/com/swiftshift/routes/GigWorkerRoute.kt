package com.swiftshift.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import com.swiftshift.data.request.LoginRequest
import com.swiftshift.data.request.gig_worker.CreateGigWorkerRequest
import com.swiftshift.data.request.gig_worker.UpdateGigWorkerRequest
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.data.response.auth.AuthResponse
import com.swiftshift.service.GigProviderService
import com.swiftshift.service.GigWorkerService
import com.swiftshift.util.*
import com.swiftshift.util.Constants.Empty
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*

fun Route.createGigWorker(
    gigWorkerService: GigWorkerService,
    gigProviderService: GigProviderService
) {
    val gson: Gson by inject()

    post("/api/gig_worker/create") {
        val multipart = call.receiveMultipart()
        var createGigWorkerRequest: CreateGigWorkerRequest? = null
        var fileName: String? = null
        var filePath: String? = null
        multipart.forEachPart { partData ->
            when (partData) {
                is PartData.FileItem -> {
                    fileName = partData.save(Constants.PROFILE_PICTURE_PATH)
                    filePath = "${Constants.PROFILE_PICTURE_PATH}$fileName"
                }

                is PartData.FormItem -> {
                    if (partData.name == Constants.CREATE_ACCOUNT_PART_DATA) {
                        createGigWorkerRequest = gson.fromJson(
                            partData.value,
                            CreateGigWorkerRequest::class.java
                        )
                    }
                }

                else -> Unit
            }
            partData.dispose()
        }

        createGigWorkerRequest?.let { request ->

            if (gigWorkerService.doesGigWorkerWithEmailExist(request.email) || gigProviderService.doesGigProviderWithEmailExist(
                    request.email
                )
            ) {
                filePath?.let { File(it).delete() }
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_ALREADY_EXISTS
                    )
                )
                return@post
            }

            when (gigWorkerService.validateCreateGigWorkerRequest(request)) {
                GigWorkerService.ValidationEvent.ErrorFieldEmpty -> {
                    filePath?.let { File(it).delete() }
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.FIELDS_BLANK
                        )
                    )
                }

                GigWorkerService.ValidationEvent.InvalidEmail -> {
                    filePath?.let { File(it).delete() }
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.INVALID_EMAIL
                        )
                    )
                }

                GigWorkerService.ValidationEvent.Success -> {
                    val profilePictureUrl =
                        if (fileName != null) "${Constants.BASE_URL}profile_pictures/$fileName" else String.Empty
                    val createAcknowledged = gigWorkerService.createGigWorker(
                        request = request,
                        profileImageUrl = profilePictureUrl
                    )
                    if (createAcknowledged) {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse<Unit>(
                                successful = true
                            )
                        )
                    } else {
                        try {
                            filePath?.let { File(it).delete() }
                        } finally {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
            }
        } ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
    }
}

fun Route.loginGigWorker(
    gigWorkerService: GigWorkerService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/gig_worker/login") {
        val request = runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val gigWorker = gigWorkerService.getGigWorkerByEmail(request.email) ?: run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessages.INVALID_CREDENTIAL
                )
            )
            return@post
        }

        val isCorrectPassword = gigWorkerService.isValidPassword(
            enteredPassword = request.password,
            actualPassword = gigWorker.password
        )

        if (isCorrectPassword) {
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim(Constants.JWT_CLAIM_GIG_WORKER_ID, gigWorker.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = AuthResponse(token = token)
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessages.INVALID_CREDENTIAL
                )
            )
        }
    }
}

fun Route.getGigWorkerProfile(
    gigWorkerService: GigWorkerService
) {
    authenticate {
        get("/api/gig_worker/profile") {
            val gigWorkerId = call.parameters[QueryParams.PARAM_GIG_WORKER_ID]

            if (gigWorkerId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val profileResponse = gigWorkerService.getGigWorkerProfile(gigWorkerId) ?: run {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                profileResponse
            )
        }
    }
}

fun Route.updateGigWorkerProfile(
    gigWorkerService: GigWorkerService,
    gigProviderService: GigProviderService
) {
    val gson: Gson by inject()

    authenticate {
        put("/api/gig_worker/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateGigWorkerRequest? = null
            var fileName: String? = null
            var filePath: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FileItem -> {
                        fileName = partData.save(Constants.PROFILE_PICTURE_PATH)
                        filePath = "${Constants.PROFILE_PICTURE_PATH}$fileName"
                    }

                    is PartData.FormItem -> {
                        if (partData.name == Constants.UPDATE_PROFILE_PART_DATA) {
                            updateProfileRequest = gson.fromJson(
                                partData.value,
                                UpdateGigWorkerRequest::class.java
                            )
                        }
                    }

                    else -> Unit
                }
                partData.dispose()
            }

            updateProfileRequest?.let { request ->

                if (gigWorkerService.doesGigWorkerWithEmailExist(request.email) || gigProviderService.doesGigProviderWithEmailExist(
                        request.email
                    )
                ) {
                    filePath?.let { File(it).delete() }
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.USER_ALREADY_EXISTS
                        )
                    )
                    return@put
                }

                when (gigWorkerService.validateGigWorkerUpdateRequest(request)) {
                    GigWorkerService.ValidationEvent.ErrorFieldEmpty -> {
                        filePath?.let { File(it).delete() }
                        call.respond(
                            BasicApiResponse<Unit>(
                                successful = false,
                                message = ApiResponseMessages.FIELDS_BLANK
                            )
                        )
                    }

                    GigWorkerService.ValidationEvent.InvalidEmail -> {
                        filePath?.let { File(it).delete() }
                        call.respond(
                            BasicApiResponse<Unit>(
                                successful = false,
                                message = ApiResponseMessages.INVALID_EMAIL
                            )
                        )
                    }

                    GigWorkerService.ValidationEvent.Success -> {
                        val profilePictureUrl =
                            if (fileName != null) "${Constants.BASE_URL}profile_pictures/$fileName" else String.Empty
                        val updateAcknowledged = gigWorkerService.updateGigWorker(
                            gigWorkerId = call.gigWorkerId,
                            profileImageUrl = profilePictureUrl,
                            request = request
                        )

                        if (updateAcknowledged) {
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
                    }
                }
            } ?: run {
                filePath?.let { File(it).delete() }
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}