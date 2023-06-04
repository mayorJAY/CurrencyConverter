package com.josycom.mayorjay.currencyconverter.common.data.repositoryImplementation

import com.josycom.mayorjay.currencyconverter.common.data.api.datasource.RemoteDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cache.datasource.LocalDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cacheupdate.CacheUpdateHandler
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import com.josycom.mayorjay.currencyconverter.common.util.isEmptyOrNull
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyConverterRepositoryTest : TestCase() {

    private lateinit var sut: CurrencyConverterRepository
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var cacheUpdateHandler: CacheUpdateHandler
    private val dispatcher = UnconfinedTestDispatcher()

    override fun setUp() {
        localDataSource = Mockito.spy(LocalDataSource::class.java)
        remoteDataSource = Mockito.spy(RemoteDataSource::class.java)
        cacheUpdateHandler = Mockito.mock(CacheUpdateHandler::class.java)
        sut = CurrencyConverterRepositoryImpl(localDataSource, remoteDataSource, dispatcher, cacheUpdateHandler)
    }

    fun `test getCurrencies valid_items_retrieved_from_database`() {
        val currencies = listOf(Currency())
        Mockito.doReturn(flowOf(currencies)).`when`(localDataSource).getCurrencies()

        runBlocking {
            val response = sut.getCurrencies().first()
            val data = response.data ?: emptyList()

            assertFalse(data.isEmptyOrNull())
            assertEquals(1, data.size)
        }
    }

    fun `test getCurrencies empty_items_retrieved_from_database`() {
        val currencies = emptyList<Currency>()
        Mockito.doReturn(flowOf(currencies)).`when`(localDataSource).getCurrencies()

        runBlocking {
            val response = sut.getCurrencies().first()
            val data = response.data ?: emptyList()

            assertTrue(data.isEmptyOrNull())
        }
    }

    fun `test getCurrencies _remote_service_is_called_when_data_is_not_available_in_cache`() {
        runBlocking {
            sut.getCurrencies()

            Mockito.verify(remoteDataSource, Mockito.never()).getCurrencies()
        }
    }

    fun `test getRates empty_items_retrieved_from_database`() {
        val rates = emptyList<Rate>()
        Mockito.doReturn(flowOf(rates)).`when`(localDataSource).getRates()

        runBlocking {
            val response = sut.getRates().first()
            val data = response.data

            assertTrue(data.isEmptyOrNull())
        }
    }

    fun `test getRates valid_items_retrieved_from_database`() {
        val rates = listOf(Rate(), Rate())
        Mockito.doReturn(flowOf(rates)).`when`(localDataSource).getRates()

        runBlocking {
            val response = sut.getRates().first()
            val data = response.data ?: emptyList()

            assertFalse(response.isEmptyOrNull())
            assertEquals(2, data.size)
        }
    }

    fun `test getRates _remote_service_is_called_when_data_is_not_available_in_cache`() {
        runBlocking {
            sut.getRates()

            Mockito.verify(remoteDataSource, Mockito.never()).getRates()
        }
    }

    fun `test getRateByCode valid_item_retrieved_from_database`() {
        val code = "AUD"
        val value = 234.07
        Mockito.doReturn(flowOf(Rate(value = value))).`when`(localDataSource).getRateByCode(code)

        runBlocking {
            val response = sut.getRateByCode(code).first()
            assertEquals(value, response.value)
        }
    }

    fun `test getRateByCode item_not_found_in_database`() {
        val code = "XYZ"
        Mockito.doReturn(flowOf(null)).`when`(localDataSource).getRateByCode(code)

        runBlocking {
            val response = sut.getRateByCode(code).first()
            assertNull(response)
        }
    }

    fun `test getRateByCode localDataSource_is_triggered_once`() {
        val code = "XYZ"
        runBlocking { sut.getRateByCode(code) }

        Mockito.verify(localDataSource, Mockito.times(1)).getRateByCode(code)
    }
}