package com.swiftshift.service

import com.swiftshift.data.model.Gig
import com.swiftshift.data.repository.gig.IGigRepository
import com.swiftshift.data.repository.gig_provider.IGigProviderRepository
import com.swiftshift.data.request.gig.CreateGigRequest
import com.swiftshift.data.response.gig.GigResponse
import com.swiftshift.util.Constants

class GigService(
    private val gigRepository: IGigRepository,
    private val gigProviderRepository: IGigProviderRepository
) {

    suspend fun createGig(
        request: CreateGigRequest,
        imageUrl: String,
        gigProviderId: String
    ): Boolean {
        val gigProvider = gigProviderRepository.getGigProviderById(gigProviderId) ?: return false

        return gigRepository.createGig(
            Gig(
                title = request.title,
                imageUrl = imageUrl,
                description = request.description,
                tag = request.tag,
                gigProviderId = gigProviderId,
                gigProviderName = gigProvider.fullName,
                maxApplier = request.maxApplier,
                currentApplier = 0,
                deadlineDate = request.deadlineDate,
                salary = request.salary,
                latitude = request.latitude,
                longitude = request.longitude,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun getGigById(
        gigId: String
    ): Gig? {
        return gigRepository.getGigById(gigId)
    }

    suspend fun getNearbyGigs(
        latitude: Double,
        longitude: Double,
        page: Int,
        pageSize: Int = Constants.DEFAULT_NEARBY_GIGS_PAGE_SIZE
    ): List<GigResponse> {
        return gigRepository.getNearbyGigs(
            latitude = latitude,
            longitude = longitude,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getRecommendedGigs(
        latitude: Double,
        longitude: Double
    ): List<GigResponse> {
        return gigRepository.getNearbyGigs(
            latitude = latitude,
            longitude = longitude,
            page = 0,
            pageSize = 3
        )
    }

    suspend fun searchGig(query: String): List<Gig> {
        return gigRepository.searchGig(query)
    }
}