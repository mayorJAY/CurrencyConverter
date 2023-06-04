package com.josycom.mayorjay.currencyconverter.common.data.di

import com.josycom.mayorjay.currencyconverter.common.data.api.datasource.RemoteDataSource
import com.josycom.mayorjay.currencyconverter.common.data.api.datasource.RemoteDataSourceImpl
import com.josycom.mayorjay.currencyconverter.common.data.cache.datasource.LocalDataSource
import com.josycom.mayorjay.currencyconverter.common.data.cache.datasource.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(localDataSource: LocalDataSourceImpl): LocalDataSource
}