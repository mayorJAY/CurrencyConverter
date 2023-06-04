package com.josycom.mayorjay.currencyconverter.common.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.josycom.mayorjay.currencyconverter.common.data.cache.dao.CurrencyConverterDao
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.CurrencyEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.RateEntity

@Database(entities = [ CurrencyEntity::class, RateEntity::class ], exportSchema = false, version = 1)
abstract class CurrencyConverterDatabase : RoomDatabase() {

    abstract fun getCurrencyConverterDao(): CurrencyConverterDao
}