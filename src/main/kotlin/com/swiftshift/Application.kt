package com.swiftshift

import com.swiftshift.di.mainModule
import com.swiftshift.di.repositoryModule
import com.swiftshift.di.serviceModule
import com.swiftshift.plugins.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(
            mainModule,
            repositoryModule,
            serviceModule
        )
    }

    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
}
