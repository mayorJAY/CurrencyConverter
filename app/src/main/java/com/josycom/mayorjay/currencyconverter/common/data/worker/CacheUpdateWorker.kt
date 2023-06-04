package com.josycom.mayorjay.currencyconverter.common.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.josycom.mayorjay.currencyconverter.common.data.worker.util.WorkerConstants
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import com.josycom.mayorjay.currencyconverter.common.util.Constants
import com.josycom.mayorjay.currencyconverter.common.util.showNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CacheUpdateWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: CurrencyConverterRepository
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            repository.updateCachedCurrencies()
            repository.updateCachedRates()
            appContext.showNotification(Constants.NOTIFICATION_CONTENT)
            Result.success(workDataOf(WorkerConstants.CACHE_UPDATE_KEY to true))
        } catch (ex: Exception) {
            appContext.showNotification(ex.message)
            Result.failure(workDataOf(WorkerConstants.CACHE_UPDATE_KEY to false))
        }
    }
}