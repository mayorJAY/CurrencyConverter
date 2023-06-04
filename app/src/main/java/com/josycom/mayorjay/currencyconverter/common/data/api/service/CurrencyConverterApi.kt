package com.josycom.mayorjay.currencyconverter.common.data.api.service

import com.josycom.mayorjay.currencyconverter.BuildConfig
import com.josycom.mayorjay.currencyconverter.common.data.api.model.RateRemote
import com.josycom.mayorjay.currencyconverter.common.data.api.util.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterApi {

    @GET(ApiConstants.CURRENCIES_ENDPOINT)
    suspend fun getCurrencies(
        @Query(ApiConstants.APP_ID) appId: String = BuildConfig.APP_ID
    ): Map<String, String>

    @GET(ApiConstants.RATES_ENDPOINT)
    suspend fun getRates(
        @Query(ApiConstants.APP_ID) appId: String = BuildConfig.APP_ID
    ): RateRemote
}