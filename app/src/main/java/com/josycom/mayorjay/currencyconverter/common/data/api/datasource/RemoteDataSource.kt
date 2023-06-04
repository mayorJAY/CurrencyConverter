package com.josycom.mayorjay.currencyconverter.common.data.api.datasource

import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate

interface RemoteDataSource {

    suspend fun getCurrencies(): List<Currency>
    suspend fun getRates(): List<Rate>
}