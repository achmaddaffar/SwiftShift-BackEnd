package com.swiftshift.di

import com.google.gson.Gson
import com.mongodb.ConnectionString
import com.swiftshift.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {

    single {
//        val client = KMongo.createClient(Constants.CLIENT_CONNECTION_STRING).coroutine
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }

    single { Gson() }
}