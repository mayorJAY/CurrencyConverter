package com.josycom.mayorjay.currencyconverter.common.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.josycom.mayorjay.currencyconverter.common.data.cache.dao.CurrencyConverterDao
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.CurrencyEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.RateEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.TimeEntity

@Database(entities = [ CurrencyEntity::class, RateEntity::class, TimeEntity::class ], exportSchema = false, version = 1)
abstract class CurrencyConverterDatabase : RoomDatabase() {

    abstract fun getCurrencyConverterDao(): CurrencyConverterDao
}