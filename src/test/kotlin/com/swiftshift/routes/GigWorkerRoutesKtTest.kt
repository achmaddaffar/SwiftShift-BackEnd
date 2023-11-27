package com.swiftshift.routes

import com.google.gson.Gson
import com.swiftshift.data.model.GigWorker
import com.swiftshift.data.response.BasicApiResponse
import com.swiftshift.plugins.configureRouting
import com.swiftshift.repository.FakeGigWorkerRepository
import com.swiftshift.util.Constants
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.test.KoinTest
import org.koin.test.inject
import org.litote.kmongo.json
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
internal class GigWorkerRoutesKtTest : KoinTest {

    private val gigWorkerRepository by inject<FakeGigWorkerRepository>()
    private val gson = Gson()

//    @Before
//    fun setup() {
//        startKoin {
//            modules(testModule)
//        }
//    }
//
//    @After
//    fun tearDown() {
//        stopKoin()
//    }

    @Test
    fun `Create gig worker, successfully added in repository`() {
        val result = runBlocking {
            gigWorkerRepository.createGigWorker(
                GigWorker(
                    fullName = "test",
                    email = "test@gmail.com",
                    password = "1234",
                    timeStamp = System.currentTimeMillis()
                )
            )
        }

        assertEquals(result, true)
    }

    @Test
    fun `Get Id from given gig worker Id`() {
        runBlocking {
            gigWorkerRepository.createGigWorker(
                GigWorker(
                    fullName = "test",
                    email = "test@gmail.com",
                    password = "1234",
                    timeStamp = System.currentTimeMillis()
                )
            )
        }

        val gigWorker = runBlocking {
            gigWorkerRepository.getGigWorkerByEmail("test@gmail.com")
        }

        val result = runBlocking {
            gigWorkerRepository.getGigWorkerById(gigWorker!!.id)
        }

        assertEquals(result, gigWorker)
    }

    @Test
    fun `Create Gig Worker, with invalid request, responds with BadRequest`() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            configureRouting()
        }
        val boundary = Constants.CREATE_ACCOUNT_PART_DATA
        val response = client.post("/api/gig_worker/create") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("fullName", "test123")
                        append("email", "test@gmail.com")
                        append("password", "test123")
                    },
                    boundary,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary)
                )
            )
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}