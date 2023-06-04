package com.josycom.mayorjay.currencyconverter.common.util

import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import junit.framework.TestCase

class UtilTest : TestCase() {

    fun `test extractCurrencyCode single pair of parenthesis _valid_code_returned`() {
        val result = "CFA Franc BCEAO (XOF)".extractCurrencyCode()
        assertEquals("XOF", result)
    }

    fun `test extractCurrencyCode two pairs of parenthesis _valid_code_returned`() {
        val result = "Chinese Yuan (Offshore) (CNH)".extractCurrencyCode()
        assertEquals("CNH", result)
    }

    fun `test isEmptyOrNull pass_null_object _empty_or_null_check_done`() {
        val rate: Rate? = null
        val result = rate.isEmptyOrNull()
        assertTrue(result)
    }

    fun `test isEmptyOrNull pass_valid_string _empty_or_null_check_done`() {
        val result = "VND".isEmptyOrNull()
        assertFalse(result)
    }

    fun `test isEmptyOrNull pass_empty_list _empty_or_null_check_done`() {
        val result = emptyList<String>().isEmptyOrNull()
        assertTrue(result)
    }
}