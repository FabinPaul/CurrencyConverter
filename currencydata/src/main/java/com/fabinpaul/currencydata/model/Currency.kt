package com.fabinpaul.currencydata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currency")
data class Currency(
    @ColumnInfo(name = "country") val country: String = "",
    @ColumnInfo(name = "currencyCode") val currencyCode: String = "",
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
