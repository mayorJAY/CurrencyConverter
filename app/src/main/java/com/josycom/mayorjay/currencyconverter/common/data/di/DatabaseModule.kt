package com.josycom.mayorjay.currencyconverter.common.data.di

import android.content.Context
import androidx.room.Room
import com.josycom.mayorjay.currencyconverter.common.data.cache.dao.CurrencyConverterDao
import com.josycom.mayorjay.currencyconverter.common.data.cache.db.CurrencyConverterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): CurrencyConverterDatabase {
        return Room.databaseBuilder(
            appContext,
            CurrencyConverterDatabase::class.java,
            "CurrencyConverter.db")
            .build()
    }

    @Provides
    fun provideCurrencyConverterDao(
        database: CurrencyConverterDatabase
    ): CurrencyConverterDao = database.getCurrencyConverterDao()
}