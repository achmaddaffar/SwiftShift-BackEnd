package com.swiftshift.di

import com.swiftshift.data.repository.applying.ApplyingRepository
import com.swiftshift.data.repository.applying.IApplyingRepository
import com.swiftshift.data.repository.gig.GigRepository
import com.swiftshift.data.repository.gig.IGigRepository
import com.swiftshift.data.repository.gig_provider.GigProviderRepository
import com.swiftshift.data.repository.gig_provider.IGigProviderRepository
import com.swiftshift.data.repository.gig_worker.GigWorkerRepository
import com.swiftshift.data.repository.gig_worker.IGigWorkerRepository
import com.swiftshift.data.repository.review.IReviewRepository
import com.swiftshift.data.repository.review.ReviewRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IApplyingRepository> { ApplyingRepository(get()) }
    single<IGigRepository> { GigRepository(get()) }
    single<IGigProviderRepository> { GigProviderRepository(get()) }
    single<IGigWorkerRepository> { GigWorkerRepository(get()) }
    single<IReviewRepository> { ReviewRepository(get()) }
}