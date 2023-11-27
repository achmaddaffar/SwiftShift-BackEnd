package com.swiftshift.service

import com.swiftshift.data.model.GigProvider
import com.swiftshift.data.repository.gig_provider.IGigProviderRepository
import com.swiftshift.data.request.gig_provider.CreateGigProviderRequest
import com.swiftshift.util.Constants

class GigProviderService(
    private val gigProviderRepository: IGigProviderRepository
) {

    suspend fun doesGigProviderWithEmailExist(email: String): Boolean {
        return gigProviderRepository.getGigProviderByEmail(email) != null
    }

    suspend fun getGigProviderByEmail(email: String): GigProvider? {
        return gigProviderRepository.getGigProviderByEmail(email)
    }

    suspend fun getGigProviderById(gigProviderId: String): GigProvider? {
        return gigProviderRepository.getGigProviderById(gigProviderId)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun createGigProvider(
        request: CreateGigProviderRequest,
        profileImageUrl: String
    ): Boolean {
        return gigProviderRepository.createGigProvider(
            GigProvider(
                fullName = request.fullName,
                email = request.email,
                password = request.password,
                timestamp = System.currentTimeMillis(),
                profileImageUrl = profileImageUrl
            )
        )
    }

    fun validateCreateGigProviderRequest(request: CreateGigProviderRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.fullName.isBlank())
            return ValidationEvent.ErrorFieldEmpty
        if (!request.email.matches(Constants.EMAIL_REGEX.toRegex()))
            return ValidationEvent.InvalidEmail
        return ValidationEvent.Success
    }

    sealed class ValidationEvent {
        data object ErrorFieldEmpty : ValidationEvent()
        data object InvalidEmail : ValidationEvent()
        data object Success : ValidationEvent()
    }
}