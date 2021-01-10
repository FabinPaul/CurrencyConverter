package com.fabinpaul.currencydata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExchangeRate")
data class ExchangeRates(
    @ColumnInfo(name = "fromCurrencyCode") val currencyCodeFrom: String = "",
    @ColumnInfo(name = "toCurrencyCode") val currencyCodeTo: String = "",
    @ColumnInfo(name = "value") val rate: Double = 0.0,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
