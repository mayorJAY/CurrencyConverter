package com.josycom.mayorjay.currencyconverter.common.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.josycom.mayorjay.currencyconverter.common.domain.model.Time

@Entity(tableName = "time")
data class TimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "time_left_millis")
    val timeLeftMillis: Long,
    @ColumnInfo(name = "app_stop_time_millis")
    val appStopTimeMillis: Long,
    @ColumnInfo(name = "is_timer_running")
    val isTimerRunning: Boolean
) {
    companion object {
        fun fromDomain(time: Time): TimeEntity {
            return TimeEntity(
                timeLeftMillis = time.timeLeftMillis,
                appStopTimeMillis = time.appStopTimeMillis,
                isTimerRunning = time.isTimerRunning
            )
        }

        fun toDomain(timeEntity: TimeEntity): Time {
            return Time(
                timeLeftMillis = timeEntity.timeLeftMillis,
                appStopTimeMillis = timeEntity.appStopTimeMillis,
                isTimerRunning = timeEntity.isTimerRunning
            )
        }
    }
}