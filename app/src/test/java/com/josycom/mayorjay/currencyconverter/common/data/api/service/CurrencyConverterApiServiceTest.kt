package com.josycom.mayorjay.currencyconverter.common.data.api.service

import com.josycom.mayorjay.currencyconverter.common.data.api.util.ApiConstants
import com.josycom.mayorjay.currencyconverter.common.util.isEmptyOrNull
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CurrencyConverterApiServiceTest : TestCase() {

    private lateinit var sut: CurrencyConverterApi
    private lateinit var server: MockWebServer

    override fun setUp() {
        server = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        sut = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(CurrencyConverterApi::class.java)
    }

    fun `test getCurrencies request_sent valid_path_called`() {
        runBlocking {
            enqueueMockResponse("Currencies.json")
            sut.getCurrencies()
            val request = server.takeRequest()
            assertTrue((request.path ?: "").contains(ApiConstants.CURRENCIES_ENDPOINT))
        }
    }

    fun `test getCurrencies request_sent valid_response_body_received`() {
        runBlocking {
            enqueueMockResponse("Currencies.json")
            val responseBody = sut.getCurrencies()
            assertNotNull(responseBody)
        }
    }

    fun `test getCurrencies request_sent valid_response_body_values_received`() {
        runBlocking {
            enqueueMockResponse("Currencies.json")
            val responseBody = sut.getCurrencies()
            assertFalse(responseBody.size.isEmptyOrNull())
            assertTrue(responseBody.contains("USD"))
        }
    }

    fun `test getCurrencies request_sent valid_currency_name_received_from_response`() {
        runBlocking {
            enqueueMockResponse("Currencies.json")
            val responseBody = sut.getCurrencies()
            val jpy = responseBody["JPY"]
            val ngn = responseBody["NGN"]
            assertEquals("Japanese Yen", jpy)
            assertEquals("Nigerian Naira", ngn)
        }
    }

    fun `test getRates request_sent valid_path_called`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            sut.getRates()
            val request = server.takeRequest()
            assertTrue((request.path ?: "").contains(ApiConstants.RATES_ENDPOINT))
        }
    }

    fun `test getRates request_sent valid_response_body_received`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            val responseBody = sut.getRates()
            assertNotNull(responseBody)
        }
    }

    fun `test getRates request_sent valid_rates_list_received_from_response`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            val responseBody = sut.getRates()
            assertFalse(responseBody.rates.isEmptyOrNull())
        }
    }

    fun `test getRates request_sent valid_base_currency_received_from_response`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            val responseBody = sut.getRates()
            assertFalse(responseBody.base.isEmptyOrNull())
        }
    }

    fun `test getRates request_sent valid_rate_value_received_from_response`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            val responseBody = sut.getRates()
            val eur = responseBody.rates?.get("EUR")
            assertFalse(eur.isEmptyOrNull())
            assertTrue((eur ?: 0.0) > 0.0)
        }
    }

    fun `test getRates request_sent valid_disclaimer_received_from_response`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            val responseBody = sut.getRates()
            assertFalse(responseBody.disclaimer.isEmptyOrNull())
        }
    }

    fun `test getRates request_sent valid_license_received_from_response`() {
        runBlocking {
            enqueueMockResponse("Rates.json")
            val responseBody = sut.getRates()
            assertFalse(responseBody.license.isEmptyOrNull())
        }
    }

    fun `test getRates request_sent_with_invalid_api_key error_response_received`() {
        runBlocking {
            enqueueMockResponse("ApiErrorResponse.json")
            val responseBody = sut.getRates()
            assertNull(responseBody.rates)
        }
    }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    override fun tearDown() {
        server.shutdown()
    }
}