package com.josycom.mayorjay.currencyconverter.common.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyRemote(
    val currencies: Map<String, String>?,
)