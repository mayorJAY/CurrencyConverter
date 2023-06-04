package com.josycom.mayorjay.currencyconverter.common.data.repositoryImplementation

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.josycom.mayorjay.currencyconverter.common.data.api.datasource.RemoteDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cache.datasource.LocalDataSource
import com.josycom.mayorjay.currencyconverter.common.data.worker.CacheUpdateWorker
import com.josycom.mayorjay.currencyconverter.common.data.worker.util.WorkerConstants
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import com.josycom.mayorjay.currencyconverter.common.util.Constants
import com.josycom.mayorjay.currencyconverter.common.util.isEmptyOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyConverterRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val workManager: WorkManager
) : CurrencyConverterRepository {

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
        .setRequiresBatteryNotLow(true)
        .build()

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

    override fun initCacheUpdater() {
        val updateWorkRequest = PeriodicWorkRequestBuilder<CacheUpdateWorker>(
            Constants.UPDATE_INTERVAL, TimeUnit.MINUTES)
            .setConstraints(workConstraints)
            .addTag(WorkerConstants.WORKER_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WorkerConstants.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            updateWorkRequest
        )
    }

    override suspend fun updateCachedCurrencies() {
        try {
            val currencies = remoteDataSource.getCurrencies()
            localDataSource.saveCurrencies(currencies)
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        }
    }

    override suspend fun updateCachedRates() {
        try {
            val rates = remoteDataSource.getRates()
            localDataSource.saveRates(rates)
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        }
    }
}