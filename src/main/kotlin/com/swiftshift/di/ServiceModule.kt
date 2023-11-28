package com.swiftshift.di

import com.swiftshift.service.*
import org.koin.dsl.module

val serviceModule = module {
    single { GigWorkerService(get()) }
    single { GigProviderService(get()) }
    single { GigService(get()) }
    single { ReviewService(get()) }
    single { ApplyingService(get()) }
}