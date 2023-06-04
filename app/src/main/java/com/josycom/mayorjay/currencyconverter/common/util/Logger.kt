package com.josycom.mayorjay.currencyconverter.common.util

import com.josycom.mayorjay.currencyconverter.BuildConfig
import timber.log.Timber

object Logger {

    fun init() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}