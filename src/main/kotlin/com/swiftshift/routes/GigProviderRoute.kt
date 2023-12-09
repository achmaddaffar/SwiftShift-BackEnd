package com.swiftshift.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import com.swiftshift.data.request.LoginRequest
import com.swiftshift.data.request.gig_provider.CreateGigProviderRequest
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.data.response.auth.AuthResponse
import com.swiftshift.routes.authenticate
import com.swiftshift.service.GigProviderService
import com.swiftshift.service.GigWorkerService
import com.swiftshift.util.ApiResponseMessages
import com.swiftshift.util.Constants
import com.swiftshift.util.Constants.Empty
import com.swiftshift.util.QueryParams
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
import java.util.*

fun Route.createGigProvider(
    gigWorkerService: GigWorkerService,
    gigProviderService: GigProviderService
) {
    val gson: Gson by inject()

    post("/api/gig_provider/create") {
        val multipart = call.receiveMultipart()
        var createGigProviderRequest: CreateGigProviderRequest? = null
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
                        createGigProviderRequest = gson.fromJson(
                            partData.value,
                            CreateGigProviderRequest::class.java
                        )
                    }
                }

                else -> Unit
            }

            partData.dispose()
        }

        createGigProviderRequest?.let { request ->
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
                return@post
            }

            when (gigProviderService.validateCreateGigProviderRequest(request)) {
                GigProviderService.ValidationEvent.ErrorFieldEmpty -> {
                    filePath?.let { File(it).delete() }
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.FIELDS_BLANK
                        )
                    )
                }

                GigProviderService.ValidationEvent.InvalidEmail -> {
                    filePath?.let { File(it).delete() }
                    call.respond(
                        BasicApiResponse<Unit>(
                            successful = false,
                            message = ApiResponseMessages.INVALID_EMAIL
                        )
                    )
                }

                GigProviderService.ValidationEvent.Success -> {
                    val profilePictureUrl =
                        if (fileName != null) "${Constants.BASE_URL}profile_pictures/$fileName" else String.Empty
                    val createAcknowledged = gigProviderService.createGigProvider(
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

fun Route.loginGigProvider(
    gigProviderService: GigProviderService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/gig_provider/login") {
        val request = runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val gigProvider = gigProviderService.getGigProviderByEmail(request.email) ?: run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = ApiResponseMessages.INVALID_CREDENTIAL
                )
            )
            return@post
        }

        val isCorrectPassword = gigProviderService.isValidPassword(
            enteredPassword = request.password,
            actualPassword = gigProvider.password
        )

        if (isCorrectPassword) {
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim(Constants.JWT_CLAIM_GIG_PROVIDER_ID, gigProvider.id)
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

fun Route.getGigProviderProfileById(
    gigProviderService: GigProviderService
) {
    authenticate {
        get("/api/gig_provider/profile_by_id") {
            val gigProviderId = call.parameters[QueryParams.PARAM_GIG_PROVIDER_ID] ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val profileResponse = gigProviderService.getGigProviderById(gigProviderId) ?: run {
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
                BasicApiResponse(
                    successful = true,
                    data = profileResponse
                )
            )
        }
    }
}

fun Route.getGigProviderProfileByEmail(
    gigProviderService: GigProviderService
) {
    authenticate {
        get("/api/gig_provider/profile_by_email") {
            val email = call.parameters[QueryParams.PARAM_EMAIL] ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val profileResponse = gigProviderService.getGigProviderByEmail(email) ?: run {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = profileResponse
                )
            )
        }
    }
}

