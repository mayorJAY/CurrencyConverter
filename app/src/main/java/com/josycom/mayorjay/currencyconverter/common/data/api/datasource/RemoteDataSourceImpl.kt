package com.josycom.mayorjay.currencyconverter.common.data.api.datasource

import com.josycom.mayorjay.currencyconverter.common.data.api.model.CurrencyRemote
import com.josycom.mayorjay.currencyconverter.common.data.api.model.mappers.CurrencyRemoteMapper
import com.josycom.mayorjay.currencyconverter.common.data.api.model.mappers.RateRemoteMapper
import com.josycom.mayorjay.currencyconverter.common.data.api.service.CurrencyConverterApi
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: CurrencyConverterApi,
    private val currencyMapper: CurrencyRemoteMapper,
    private val rateMapper: RateRemoteMapper
): RemoteDataSource {

    override suspend fun getCurrencies(): List<Currency> {
        val currencies = apiService.getCurrencies()
        return currencyMapper.mapToDomain(CurrencyRemote(currencies)).currencies
    }

    override suspend fun getRates(): List<Rate> {
        val rateRemote = apiService.getRates()
        return rateMapper.mapToDomain(rateRemote).rates
    }
}