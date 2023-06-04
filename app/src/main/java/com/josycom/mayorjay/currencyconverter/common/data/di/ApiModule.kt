package com.josycom.mayorjay.currencyconverter.common.data.di

import com.josycom.mayorjay.currencyconverter.BuildConfig
import com.josycom.mayorjay.currencyconverter.common.data.api.interceptor.NetworkStatusInterceptor
import com.josycom.mayorjay.currencyconverter.common.data.api.service.CurrencyConverterApi
import com.josycom.mayorjay.currencyconverter.common.data.api.util.ApiConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when {
                BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideClient(networkStatusInterceptor: NetworkStatusInterceptor,
                      loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(ApiConstants.DURATION, TimeUnit.SECONDS)
            .readTimeout(ApiConstants.DURATION, TimeUnit.SECONDS)
            .writeTimeout(ApiConstants.DURATION, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String,
                        moshi: Moshi,
                        client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CurrencyConverterApi = retrofit.create(CurrencyConverterApi::class.java)
}