package com.josycom.mayorjay.currencyconverter.common.data.cache.datasource

import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getCurrencies(): Flow<List<Currency>>
    suspend fun saveCurrencies(currencies: List<Currency>)
    fun getRates(): Flow<List<Rate>>
    fun getRateByCode(code: String): Flow<Rate>
    suspend fun saveRates(rates: List<Rate>)
    fun getLastUpdateTime(): Flow<Long?>
}