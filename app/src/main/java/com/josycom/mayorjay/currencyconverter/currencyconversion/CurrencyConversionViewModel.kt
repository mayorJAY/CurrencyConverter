package com.josycom.mayorjay.currencyconverter.currencyconversion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.domain.model.Time
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import com.josycom.mayorjay.currencyconverter.common.util.Constants
import com.josycom.mayorjay.currencyconverter.common.util.Resource
import com.josycom.mayorjay.currencyconverter.common.util.extractCurrencyCode
import com.josycom.mayorjay.currencyconverter.common.util.isEmptyOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val repository: CurrencyConverterRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val currenciesLiveData: MutableLiveData<Resource<List<Currency>>> = MutableLiveData()
    fun getCurrenciesLiveData(): MutableLiveData<Resource<List<Currency>>> = currenciesLiveData
    private fun setCurrenciesLiveDataValue(value: Resource<List<Currency>>) { currenciesLiveData.value = value }

    private val ratesLiveData: MutableLiveData<Resource<List<Rate>>> = MutableLiveData()
    fun getRatesLiveData(): MutableLiveData<Resource<List<Rate>>> = ratesLiveData
    private fun setRatesLiveDataValue(value: Resource<List<Rate>>) { ratesLiveData.value = value }

    private val errorMsgLiveData: MutableLiveData<String> = MutableLiveData()
    fun getErrorMsgLiveData(): MutableLiveData<String> = errorMsgLiveData
    fun setErrorMsgLiveDataValue(value: String) { errorMsgLiveData.value = value }

    private val timeLiveData: MutableLiveData<Time?> = MutableLiveData()
    fun getTimeLiveData(): MutableLiveData<Time?> = timeLiveData
    fun setTimeLiveDataValue(value: Time?) { timeLiveData.value = value }

    private var _currencySelected: String? = null
    var currencySelected: String?
        get() = _currencySelected
        set(value) { _currencySelected = value }

    private var _amountInputted: String? = null
    var amountInputted: String?
        get() = _amountInputted
        set(value) { _amountInputted = value }

    fun getCurrencies() {
        viewModelScope.launch {
            val currencyFlow = repository.getCurrencies()
            currencyFlow.collect { currencyResource ->
                setCurrenciesLiveDataValue(currencyResource)
            }
        }
    }

    fun getRates() {
        viewModelScope.launch {
            val rateFlow = repository.getRates()
            rateFlow.collect { rateResource ->
                setRatesLiveDataValue(rateResource)
            }
        }
    }

    fun getSpinnerItems(currencies: List<Currency>): List<String> {
        return mutableListOf<String>().apply {
            add(Constants.SPINNER_INDEX_VALUE)
            addAll(currencies.map { "${it.name} (${it.code})" })
        }
    }

    fun validateInputs(): Boolean {
        if (amountInputted.isEmptyOrNull() || (amountInputted?.toDouble() ?: Constants.EMPTY_DOUBLE_VALUE) <= Constants.EMPTY_DOUBLE_VALUE) {
            setErrorMsgLiveDataValue(Constants.AMOUNT_ERROR_MSG)
            return false
        } else if (currencySelected.isEmptyOrNull() || currencySelected.equals(Constants.SPINNER_INDEX_VALUE)) {
            setErrorMsgLiveDataValue(Constants.CURRENCY_ERROR_MSG)
            return false
        }
        return true
    }

    fun convert(amount: Double, fromCurrency: String) {
        setRatesLiveDataValue(Resource.Loading())
        viewModelScope.launch {
            try {
                repository.getRateByCode(fromCurrency.extractCurrencyCode()).collect { fromCurrencyRate ->
                    repository.getRates().collect { rateResource ->
                        rateResource.data?.let {
                            val success = Resource.Success(it.map { rate -> convertCurrency(amount, fromCurrencyRate.value, rate) })
                            setRatesLiveDataValue(success)
                        }
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                setRatesLiveDataValue(Resource.Error(ex))
            }
        }
    }

    fun convertCurrency(amount: Double, fromCurrencyRate: Double, toCurrencyRate: Rate): Rate {
        val baseAmount = amount / fromCurrencyRate
        return toCurrencyRate.apply { this.amount = baseAmount * this.value }
    }

    suspend fun fetchTimeData(): Time? {
        return withContext(dispatcher) {
            repository.getTime()
        }
    }

    fun saveTimeData(time: Time) {
        viewModelScope.launch {
            repository.saveTime(time)
        }
    }

    fun performCacheUpdate() {
        viewModelScope.launch {
            repository.performCacheUpdate()
        }
    }
}