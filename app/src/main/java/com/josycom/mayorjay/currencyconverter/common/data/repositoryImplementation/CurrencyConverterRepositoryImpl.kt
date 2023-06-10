package com.josycom.mayorjay.currencyconverter.common.data.repositoryImplementation

import com.josycom.mayorjay.currencyconverter.common.data.api.datasource.RemoteDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cache.datasource.LocalDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cacheupdate.CacheUpdateHandler
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import com.josycom.mayorjay.currencyconverter.common.util.isEmptyOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class CurrencyConverterRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val cacheUpdateHandler: CacheUpdateHandler
) : CurrencyConverterRepository {

    override suspend fun getCurrencies() = networkBoundResource(
        query = {
            localDataSource.getCurrencies()
        },
        shouldFetch = { currencies ->
            currencies.isEmptyOrNull()
        },
        fetch = {
            delay(2000)
            remoteDataSource.getCurrencies()
        },
        saveFetchResult = { currencies ->
            localDataSource.saveCurrencies(currencies)
        },
        onFetchFailed = { ex ->
            Timber.e(ex)
        },
        dispatcher = dispatcher
    )

    override suspend fun getRates() = networkBoundResource(
        query = {
            localDataSource.getRates()
        },
        shouldFetch = { rates ->
            rates.isEmptyOrNull()
        },
        fetch = {
            delay(2000)
            remoteDataSource.getRates()
        },
        saveFetchResult = { rates ->
            localDataSource.saveRates(rates)
        },
        onFetchFailed = { ex ->
            Timber.e(ex)
        },
        dispatcher = dispatcher
    )

    override fun getRateByCode(code: String): Flow<Rate> {
        return localDataSource.getRateByCode(code)
    }

    override suspend fun performCacheUpdate() {
        cacheUpdateHandler.updateCache()
    }

    override fun getLastUpdateTime(): Flow<Long?> {
        return localDataSource.getLastUpdateTime()
    }
}