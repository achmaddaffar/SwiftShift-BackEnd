package com.swiftshift.di

import com.swiftshift.service.GigProviderService
import com.swiftshift.service.GigService
import com.swiftshift.service.GigWorkerService
import org.koin.dsl.module

val serviceModule = module {
    single { GigWorkerService(get()) }
    single { GigProviderService(get()) }
    single { GigService(get()) }
}