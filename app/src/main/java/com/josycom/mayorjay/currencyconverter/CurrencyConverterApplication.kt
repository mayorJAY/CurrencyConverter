package com.josycom.mayorjay.currencyconverter

import android.app.Application
import com.josycom.mayorjay.currencyconverter.common.util.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CurrencyConverterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() = Logger.init()
}