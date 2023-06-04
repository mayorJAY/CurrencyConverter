package com.josycom.mayorjay.currencyconverter.common.data.cacheupdate

import android.content.Context
import com.josycom.mayorjay.currencyconverter.common.data.api.datasource.RemoteDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cache.datasource.LocalDataSource
import com.josycom.mayorjay.currencyconverter.common.util.Constants
import com.josycom.mayorjay.currencyconverter.common.util.showNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

open class CacheUpdateHandler @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {

    suspend fun updateCache() {
        try {
            val currencies = remoteDataSource.getCurrencies()
            localDataSource.saveCurrencies(currencies)

            val rates = remoteDataSource.getRates()
            localDataSource.saveRates(rates)
            appContext.showNotification(Constants.NOTIFICATION_CONTENT)
        } catch (ex: Exception) {
            appContext.showNotification(ex.message)
        }
    }
}