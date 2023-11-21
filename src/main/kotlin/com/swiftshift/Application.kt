package com.swiftshift

import com.swiftshift.di.mainModule
import com.swiftshift.di.repositoryModule
import com.swiftshift.plugins.*
import io.ktor.server.application.*
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    startKoin {
        modules(
            mainModule,
            repositoryModule
        )
    }

    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
}
