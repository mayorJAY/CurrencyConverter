package com.josycom.mayorjay.currencyconverter.common.data.api.model.mappers

import com.josycom.mayorjay.currencyconverter.common.data.api.model.CurrencyRemote
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.CurrencyDomain
import javax.inject.Inject

class CurrencyRemoteMapper @Inject constructor() : ApiMapper<CurrencyRemote, CurrencyDomain> {

    override fun mapToDomain(apiEntity: CurrencyRemote): CurrencyDomain {
        val currencies = mutableListOf<Currency>()
        apiEntity.currencies?.let { map ->
            for ((key, value) in map) {
                currencies.add(Currency(code = key, name = value))
            }
        }
        return CurrencyDomain(currencies)
    }
}