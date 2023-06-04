package com.josycom.mayorjay.currencyconverter.common.domain.model

data class Rate(
    val code: String = "",
    val value: Double = 0.0,
    var amount: Double = 0.0
)