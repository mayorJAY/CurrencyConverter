package com.josycom.mayorjay.currencyconverter.common.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,
    val name: String
) {
    companion object {
        fun fromDomain(currency: Currency): CurrencyEntity {
            return CurrencyEntity(currency.code, currency.name)
        }

        fun toDomain(currencyEntity: CurrencyEntity): Currency {
            return Currency(currencyEntity.code, currencyEntity.name)
        }
    }
}