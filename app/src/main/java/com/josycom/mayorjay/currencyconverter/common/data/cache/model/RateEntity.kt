package com.josycom.mayorjay.currencyconverter.common.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate

@Entity(tableName = "rate")
data class RateEntity(
    @PrimaryKey
    val code: String,
    val value: Double
) {
    companion object {
        fun fromDomain(rate: Rate): RateEntity {
            return RateEntity(rate.code, rate.value)
        }

        fun toDomain(rateEntity: RateEntity): Rate {
            return Rate(rateEntity.code, rateEntity.value)
        }
    }
}