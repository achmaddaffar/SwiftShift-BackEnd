package com.swiftshift.di

import com.swiftshift.data.repository.applying.ApplyingRepository
import com.swiftshift.data.repository.gig.GigRepository
import com.swiftshift.data.repository.gig_provider.GigProviderRepository
import com.swiftshift.data.repository.gig_worker.GigWorkerRepository
import com.swiftshift.data.repository.review.ReviewRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { ApplyingRepository(get()) }
    single { GigRepository(get()) }
    single { GigProviderRepository(get()) }
    single { GigWorkerRepository(get()) }
    single { ReviewRepository(get()) }
}