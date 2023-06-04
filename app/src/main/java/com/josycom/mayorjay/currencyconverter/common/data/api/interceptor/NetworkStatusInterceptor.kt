package com.josycom.mayorjay.currencyconverter.common.data.api.interceptor

import com.josycom.mayorjay.currencyconverter.common.data.api.util.ConnectionManager
import com.josycom.mayorjay.currencyconverter.common.domain.util.NetworkUnavailableException
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(private val connectionManager: ConnectionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected) {
            chain.proceed(chain.request())
        } else {
            throw NetworkUnavailableException()
        }
    }
}