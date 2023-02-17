package com.kyuseon.coinapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date


@Entity(tableName = "selected_coin_price_table")
data class SelectedCoinPriceEntity (

    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val coinName : String,
    val transaction_date : String,
    val type : String,
    val unites_traded : String,
    val price : String,
    val total : String,
    val timeStamp : Date
        )

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value : Long) : Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date : Date) : Long {
        return date.time
    }
}