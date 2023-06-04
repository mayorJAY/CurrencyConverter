package com.josycom.mayorjay.currencyconverter.common.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RateRemote(
    val base: String?,
    val disclaimer: String?,
    val license: String?,
    val rates: Map<String, Double>?,
    val timestamp: Long?
)