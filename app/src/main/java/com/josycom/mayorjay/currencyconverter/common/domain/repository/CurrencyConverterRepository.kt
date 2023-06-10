package com.josycom.mayorjay.currencyconverter.common.domain.repository

import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyConverterRepository {

    suspend fun getCurrencies(): Flow<Resource<List<Currency>>>
    suspend fun getRates(): Flow<Resource<List<Rate>>>
    fun getRateByCode(code: String): Flow<Rate>
    suspend fun performCacheUpdate()
    fun getLastUpdateTime(): Flow<Long?>
}