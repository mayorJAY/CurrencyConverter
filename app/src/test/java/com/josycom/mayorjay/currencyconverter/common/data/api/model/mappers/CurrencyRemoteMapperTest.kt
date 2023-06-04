package com.josycom.mayorjay.currencyconverter.common.data.api.model.mappers

import com.josycom.mayorjay.currencyconverter.common.data.api.model.CurrencyRemote
import junit.framework.TestCase

class CurrencyRemoteMapperTest : TestCase() {

    private lateinit var sut: CurrencyRemoteMapper

    override fun setUp() {
        sut = CurrencyRemoteMapper()
    }

    fun `test mapToDomain _map_successfully_converted_to_list_of_objects`() {
        val currencyRemote = CurrencyRemote(mapOf("BBD" to "Barbadian Dollar", "AMD" to "Armenian Dram"))

        val currencyDomain = sut.mapToDomain(currencyRemote)
        assertEquals(2, currencyDomain.currencies.size)
    }

    fun `test mapToDomain _list_returned_has_valid_values`() {
        val currencyRemote = CurrencyRemote(mapOf("BBD" to "Barbadian Dollar", "AMD" to "Armenian Dram"))

        val currencyDomain = sut.mapToDomain(currencyRemote)
        assertEquals("Barbadian Dollar", currencyDomain.currencies[0].name)
    }
}