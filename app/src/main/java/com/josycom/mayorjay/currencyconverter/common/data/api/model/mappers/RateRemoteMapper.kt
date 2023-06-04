package com.josycom.mayorjay.currencyconverter.common.data.api.model.mappers

import com.josycom.mayorjay.currencyconverter.common.data.api.model.RateRemote
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.domain.model.RateDomain
import javax.inject.Inject

class RateRemoteMapper @Inject constructor() : ApiMapper<RateRemote, RateDomain> {

    override fun mapToDomain(apiEntity: RateRemote): RateDomain {
        val rates = mutableListOf<Rate>()
        apiEntity.rates?.let { map ->
            for ((key, value) in map) {
                rates.add(Rate(code = key, value = value))
            }
        }
        return RateDomain(rates)
    }
}