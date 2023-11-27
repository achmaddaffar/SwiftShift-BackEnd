package com.swiftshift.di

import com.swiftshift.repository.FakeGigWorkerRepository
import org.koin.dsl.module

internal val testModule = module {
    single { FakeGigWorkerRepository() }
}