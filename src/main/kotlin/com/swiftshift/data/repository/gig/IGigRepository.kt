package com.swiftshift.data.repository.gig

import com.swiftshift.data.model.Gig
import com.swiftshift.util.Constants

interface IGigRepository {

    suspend fun createGig(gig: Gig): Boolean

    suspend fun deleteGig(gigId: String): Boolean

    suspend fun getGigById(gigId: String): Gig?

    suspend fun getGigsByGigProvider(
        gigProviderId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_GIGS_BY_PROVIDER_SIZE
    ): List<Gig>
}