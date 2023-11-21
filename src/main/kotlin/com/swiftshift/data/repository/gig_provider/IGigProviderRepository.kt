package com.swiftshift.data.repository.gig_provider

import com.swiftshift.data.model.GigProvider
import com.swiftshift.data.request.gig_provider.UpdateGigProviderRequest

interface IGigProviderRepository {

    suspend fun createGigProvider(gigProvider: GigProvider): Boolean

    suspend fun getGigProviderById(id: String): GigProvider?

    suspend fun getGigProviderByEmail(email: String): GigProvider?

    suspend fun updateGigProvider(
        gigProviderId: String,
        profileImageUrl: String,
        request: UpdateGigProviderRequest
    ): Boolean

    suspend fun doesPasswordForGigProviderMatch(email: String, enteredPassword: String): Boolean

    suspend fun doesEmailBelongToGigProviderId(email: String, gigProviderId: String): Boolean

    suspend fun searchForGigProvider(query: String): List<GigProvider>

    suspend fun getGigProviders(gigProviderIds: List<String>): List<GigProvider>
}