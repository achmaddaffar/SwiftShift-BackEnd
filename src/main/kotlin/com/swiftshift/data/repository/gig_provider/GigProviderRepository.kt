package com.swiftshift.data.repository.gig_provider

import com.swiftshift.data.model.GigProvider
import com.swiftshift.data.request.gig_provider.UpdateGigProviderRequest
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.or
import org.litote.kmongo.regex

class GigProviderRepository(
    db: CoroutineDatabase
): IGigProviderRepository {

    private val gigProviders = db.getCollection<GigProvider>()

    override suspend fun createGigProvider(gigProvider: GigProvider): Boolean {
        return gigProviders.insertOne(gigProvider).wasAcknowledged()
    }

    override suspend fun getGigProviderById(id: String): GigProvider? {
        return gigProviders.findOneById(id)
    }

    override suspend fun getGigProviderByEmail(email: String): GigProvider? {
        return gigProviders.findOneById(GigProvider::email eq email)
    }

    override suspend fun updateGigProvider(
        gigProviderId: String,
        profileImageUrl: String,
        request: UpdateGigProviderRequest
    ): Boolean {
        val gigProvider = getGigProviderById(gigProviderId) ?: return false
        return gigProviders.updateOneById(
            id = gigProviderId,
            update = GigProvider(
                email = request.email,
                fullName = request.fullName,
                password = request.password,
                timestamp = System.currentTimeMillis(),
                profileImageUrl = profileImageUrl,
                id = gigProvider.id
            )
        ).wasAcknowledged()
    }

    override suspend fun doesPasswordForGigProviderMatch(email: String, enteredPassword: String): Boolean {
        val gigProvider = getGigProviderByEmail(email)
        return gigProvider?.password == enteredPassword
    }

    override suspend fun doesEmailBelongToGigProviderId(email: String, gigProviderId: String): Boolean {
        return gigProviders.findOneById(gigProviderId)?.email == email
    }

    override suspend fun searchForGigProvider(query: String): List<GigProvider> {
        return gigProviders.find(
            or(
                GigProvider::fullName regex Regex("(?i).*$query.*"),
                GigProvider::email eq query
            )
        )
            .ascendingSort(GigProvider::fullName)
            .toList()
    }

    override suspend fun getGigProviders(gigProviderIds: List<String>): List<GigProvider> {
        return gigProviders.find(GigProvider::id `in` gigProviderIds).toList()
    }
}