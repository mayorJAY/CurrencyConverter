package com.josycom.mayorjay.currencyconverter.common.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time")
data class TimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "last_update_time")
    val lastUpdateTime: Long = System.currentTimeMillis()
)