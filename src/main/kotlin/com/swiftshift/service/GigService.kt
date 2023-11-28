package com.swiftshift.service

import com.swiftshift.data.model.Gig
import com.swiftshift.data.repository.gig.IGigRepository
import com.swiftshift.data.request.gig.CreateGigRequest
import com.swiftshift.data.request.gig.GetNearbyGigsRequest
import com.swiftshift.data.request.gig.GetRecommendedGigsRequest
import com.swiftshift.data.response.gig.GigResponse
import com.swiftshift.util.Constants

class GigService(
    private val gigRepository: IGigRepository
) {

    suspend fun createGig(
        request: CreateGigRequest,
        imageUrl: String,
        gigProviderId: String
    ): Boolean {
        return gigRepository.createGig(
            Gig(
                title = request.title,
                imageUrl = imageUrl,
                description = request.description,
                tag = request.tag,
                gigProviderId = gigProviderId,
                gigProviderName = request.gigProviderName,
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
        request: GetNearbyGigsRequest,
        page: Int,
        pageSize: Int = Constants.DEFAULT_NEARBY_GIGS_PAGE_SIZE
    ): List<GigResponse> {
        return gigRepository.getNearbyGigs(
            latitude = request.latitude,
            longitude = request.longitude,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getRecommendedGigs(
        request: GetRecommendedGigsRequest
    ): List<GigResponse> {
        return gigRepository.getNearbyGigs(
            latitude = request.latitude,
            longitude = request.longitude,
            page = 0,
            pageSize = 3
        )
    }
}