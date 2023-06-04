package com.josycom.mayorjay.currencyconverter.common.data.cache.datasource

import androidx.room.withTransaction
import com.josycom.mayorjay.currencyconverter.common.data.cache.dao.CurrencyConverterDao
import com.josycom.mayorjay.currencyconverter.common.data.cache.db.CurrencyConverterDatabase
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.CurrencyEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.RateEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.TimeEntity
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val database: CurrencyConverterDatabase,
    private val dao: CurrencyConverterDao
) : LocalDataSource {

    override fun getCurrencies(): Flow<List<Currency>> {
        return dao.getCurrencies().map { currencies ->
            currencies.map { CurrencyEntity.toDomain(it) }
        }
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        database.withTransaction {
            dao.deleteAllCurrencies()
            dao.insertCurrencies(currencies.map { CurrencyEntity.fromDomain(it) })
        }
    }

    override fun getRates(): Flow<List<Rate>> {
        return dao.getRates().map { rates ->
            rates.map { RateEntity.toDomain(it) }
        }
    }

    override fun getRateByCode(code: String): Flow<Rate> {
        return dao.getRateByCode(code).map { RateEntity.toDomain(it) }
    }

    override suspend fun saveRates(rates: List<Rate>) {
        database.withTransaction {
            dao.deleteAllRates()
            dao.insertRates(rates.map { RateEntity.fromDomain(it) })
            dao.deleteTime()
            dao.insertTime(TimeEntity())
        }
    }

    override fun getLastUpdateTime(): Flow<Long> {
        return dao.getLastUpdateTime()
    }
}