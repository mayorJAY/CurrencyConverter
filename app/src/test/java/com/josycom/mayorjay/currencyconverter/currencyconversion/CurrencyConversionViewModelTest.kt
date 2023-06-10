package com.josycom.mayorjay.currencyconverter.currencyconversion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.domain.repository.CurrencyConverterRepository
import com.josycom.mayorjay.currencyconverter.common.util.Constants
import com.josycom.mayorjay.currencyconverter.common.util.Resource
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CurrencyConversionViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CurrencyConverterRepository

    @InjectMocks
    private lateinit var sut: CurrencyConversionViewModel

    private val dispatcher = UnconfinedTestDispatcher()

    private val currencyMap = mapOf(
        "USD" to 1.0,
        "ARS" to 507.0475,
        "AUD" to 1.514296,
        "NGN" to 460.44859,
        "BMD" to 1.0,
        "BRL" to 5.044645
    )

    @Before
    fun before() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test validateInputs _invalid_amount_inputted _error_message_returned`() {
        sut.amountInputted = 000.0.toString()

        val result = sut.validateInputs()
        val errorMsg = sut.getErrorMsgLiveData().value

        assertFalse(result)
        assertEquals(Constants.AMOUNT_ERROR_MSG, errorMsg)
    }

    @Test
    fun `test validateInputs _invalid_currency_selected _error_message_returned`() {
        sut.amountInputted = 300.0.toString()
        sut.currencySelected = Constants.SPINNER_INDEX_VALUE

        val result = sut.validateInputs()
        val errorMsg = sut.getErrorMsgLiveData().value

        assertFalse(result)
        assertEquals(Constants.CURRENCY_ERROR_MSG, errorMsg)
    }

    @Test
    fun `test validateInputs _valid_amount_and_currency_inputted _no_error_message_returned`() {
        sut.amountInputted = 300.0.toString()
        sut.currencySelected = "Swiss Franc (CHF)"

        val result = sut.validateInputs()
        val errorMsg = sut.getErrorMsgLiveData().value

        assertTrue(result)
        assertEquals(null, errorMsg)
    }

    @Test
    fun `test getSpinnerItems _empty_list_passed _single_value_returned`() {
        val result = sut.getSpinnerItems(emptyList())

        assertEquals(1, result.size)
        assertEquals(Constants.SPINNER_INDEX_VALUE, result.first())
    }

    @Test
    fun `test getSpinnerItems _valid_list_passed _valid_values_returned`() {
        val result = sut.getSpinnerItems(listOf(Currency(code = "CHF", name = "Swiss Franc")))

        assertEquals(2, result.size)
        assertEquals("Swiss Franc (CHF)", result[1])
    }

    @Test
    fun `test convertCurrency _accurate_conversion_performed`() {
        val rate = Rate(code = "BRL", value = 5.044645)

        val expected = convertCurrency(1000.0, "NGN", "BRL")
        val outcome = sut.convertCurrency(1000.0, currencyMap["NGN"] ?: 0.0, rate)

        assertEquals(expected, outcome.amount)
    }

    @Test
    fun `test convertCurrency _valid_currency_passed_ valid_result_returned`() {
        val rate = Rate(code = "USD", value = 1.0)

        convertCurrency(10000.0, "ARS", "USD")
        val outcome = sut.convertCurrency(10000.0, currencyMap["ARS"] ?: 0.0, rate)

        assertTrue(outcome.amount > 0.0)
    }

    @Test
    fun `test convert _convert_value_from_USD_to_other_currencies _valid_values_returned`() = runBlocking {
        val code = "American Dollar (USD)"
        val list = listOf(Rate(code = "ARS", value = 507.0475), Rate(code = "AUD", value = 1.514296), Rate(code = "BMD", value = 1.0), Rate(code = "BRL", value = 5.044645))
        Mockito.`when`(repository.getRateByCode(Mockito.anyString())).thenReturn(flowOf(Rate(code = "USD", value = 1.0)))
        Mockito.`when`(repository.getRates()).thenReturn(flowOf(Resource.Success(list)))

        sut.convert(200.0, code)
        val result = sut.getRatesLiveData().value

        assertEquals(4, result?.data?.size)
    }

    @Test
    fun `test convert _convert_value_from_USD_to_other_currencies _valid_amounts_returned`(): Unit = runBlocking {
        val code = "American Dollar (USD)"
        val list = listOf(Rate(code = "ARS", value = 507.0475), Rate(code = "AUD", value = 1.514296), Rate(code = "BMD", value = 1.0), Rate(code = "BRL", value = 5.044645))
        Mockito.`when`(repository.getRateByCode(Mockito.anyString())).thenReturn(flowOf(Rate(code = "USD", value = 1.0)))
        Mockito.`when`(repository.getRates()).thenReturn(flowOf(Resource.Success(list)))

        sut.convert(200.0, code)
        val result = sut.getRatesLiveData().value

        result?.data?.let {
            for (item in it) {
                assertTrue(item.amount > 0.0)
            }
        }
    }

    @Test
    fun `test convert _wrong_currency_value_passed _error_message_returned`() {
        val code = "USD" //ideal value should be "American Dollar (USD)"

        sut.convert(200.0, code)
        val result = sut.getRatesLiveData().value

        assertTrue(result is Resource.Error)
    }

    @Test
    fun `test performCacheUpdate _repository#performCacheUpdate_is_triggered`() = runBlocking {
        sut.performCacheUpdate()
        Mockito.verify(repository, Mockito.atLeast(1)).performCacheUpdate()
    }

    @Test
    fun `test getCurrencies _repository#getCurrencies_is_triggered`(): Unit = runBlocking {
        sut.getCurrencies()
        Mockito.verify(repository, Mockito.times(1)).getCurrencies()
    }

    @Test
    fun `test getRates _repository#getRates_is_triggered`(): Unit = runBlocking {
        sut.getRates(false)
        Mockito.verify(repository, Mockito.times(1)).getRates()
    }

    private fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val baseAmount = amount / (currencyMap[fromCurrency] ?: 0.0)
        return baseAmount * (currencyMap[toCurrency] ?: 0.0)
    }
}