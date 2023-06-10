package com.josycom.mayorjay.currencyconverter.common.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.CurrencyEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.RateEntity
import com.josycom.mayorjay.currencyconverter.common.data.cache.model.TimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyConverterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)

    @Query("SELECT * FROM currency ORDER BY code ASC")
    fun getCurrencies(): Flow<List<CurrencyEntity>>

    @Query("DELETE FROM currency")
    suspend fun deleteAllCurrencies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<RateEntity>)

    @Query("SELECT * FROM rate ORDER BY code ASC")
    fun getRates(): Flow<List<RateEntity>>

    @Query("SELECT * FROM rate WHERE code = :code")
    fun getRateByCode(code: String): Flow<RateEntity>

    @Query("DELETE FROM rate")
    suspend fun deleteAllRates()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTime(time: TimeEntity)

    @Query("SELECT last_update_time FROM time LIMIT 1")
    fun getLastUpdateTime(): Flow<Long?>

    @Query("DELETE FROM time")
    suspend fun deleteTime()
}