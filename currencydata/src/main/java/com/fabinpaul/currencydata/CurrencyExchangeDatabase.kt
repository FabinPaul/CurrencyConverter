package com.fabinpaul.currencydata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fabinpaul.currencydata.model.Currency
import com.fabinpaul.currencydata.model.ExchangeRates
import com.fabinpaul.currencydata.source.local.CurrencyExchangeDao

@Database(
    entities = [Currency::class, ExchangeRates::class],
    version = BuildConfig.dbVersion,
    exportSchema = false
)
abstract class CurrencyExchangeDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "exchange.db"

        @Volatile
        private var INSTANCE: CurrencyExchangeDatabase? = null

        fun getAppDatabase(ctx: Context): CurrencyExchangeDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    ctx.applicationContext,
                    CurrencyExchangeDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }

    abstract fun currencyExchangeDao(): CurrencyExchangeDao
}