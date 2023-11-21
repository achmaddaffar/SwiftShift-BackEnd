package com.swiftshift.service

import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.repository.gig_worker.IGigWorkerRepository
import com.swiftshift.data.request.CreateGigWorkerRequest
import com.swiftshift.data.request.UpdateGigWorkerRequest
import com.swiftshift.data.response.ProfileResponse

class GigWorkerService(
    private val gigWorkerRepository: IGigWorkerRepository
) {

    suspend fun doesGigWorkerWithEmailExist(email: String): Boolean {
        return gigWorkerRepository.getGigWorkerByEmail(email) != null
    }

    suspend fun getGigWorkerProfile(gigWorkerId: String): ProfileResponse? {
        val gigWorker = gigWorkerRepository.getGigWorkerById(gigWorkerId) ?: return null
        return ProfileResponse(
            fullName = gigWorker.fullName,
            profileImageUrl = gigWorker.profileImageUrl,
            joiningDate = gigWorker.timeStamp,
            email = gigWorker.email,
            totalIncome = gigWorker.totalIncome,
            gender = gigWorker.gender,
            highestEducation = gigWorker.highestEducation,
            cvUrl = gigWorker.cvUrl
        )
    }

    suspend fun updateGigWorker(
        gigWorkerId: String,
        profileImageUrl: String,
        request: UpdateGigWorkerRequest
    ): Boolean {
        return gigWorkerRepository.updateGigWorker(
            gigWorkerId = gigWorkerId,
            profileImageUrl = profileImageUrl,
            request = request
        )
    }

    suspend fun getGigWorkerByEmail(email: String): GigWorker? {
        return gigWorkerRepository.getGigWorkerByEmail(email)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun createGigWorker(request: CreateGigWorkerRequest): Boolean {
        return gigWorkerRepository.createGigWorker(
            GigWorker(
                fullName = request.fullName,
                email = request.email,
                password = request.password,
                timeStamp = System.currentTimeMillis(),
                profileImageUrl = request.profileImageUrl
            )
        )
    }

    fun validateCreateGigWorkerRequest(request: CreateGigWorkerRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.fullName.isBlank())
            return ValidationEvent.ErrorFieldEmpty
        return ValidationEvent.Success
    }

    sealed class ValidationEvent {
        data object ErrorFieldEmpty : ValidationEvent()
        data object Success : ValidationEvent()
    }
}