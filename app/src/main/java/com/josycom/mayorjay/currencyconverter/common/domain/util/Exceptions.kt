package com.josycom.mayorjay.currencyconverter.common.domain.util

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") : IOException(message)

class NetworkException(message: String): Exception(message)