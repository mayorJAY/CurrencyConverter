package com.josycom.mayorjay.currencyconverter.common.data.di

import com.josycom.mayorjay.currencyconverter.common.data.repositoryImplementation.CurrencyConverterRepositoryImpl
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: CurrencyConverterRepositoryImpl): CurrencyConverterRepository
}