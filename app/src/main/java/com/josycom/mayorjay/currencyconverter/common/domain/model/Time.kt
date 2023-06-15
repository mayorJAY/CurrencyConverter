package com.josycom.mayorjay.currencyconverter.common.domain.model

data class Time(
    var timeLeftMillis: Long = 0L,
    var appStopTimeMillis: Long = 0L,
    var isTimerRunning: Boolean = false
)