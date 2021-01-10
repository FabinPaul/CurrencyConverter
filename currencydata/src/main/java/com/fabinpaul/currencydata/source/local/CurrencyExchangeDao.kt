package com.fabinpaul.currencydata.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fabinpaul.currencydata.model.Currency
import com.fabinpaul.currencydata.model.ExchangeRates

@Dao
interface CurrencyExchangeDao {

    @Query("SELECT * FROM Currency")
    suspend fun listCurrencies(): List<Currency>

    @Query("SELECT * FROM ExchangeRate")
    suspend fun getExchangeRatesForUsd(): List<ExchangeRates>

    @Query("SELECT * FROM ExchangeRate")
    fun observeExchangeRatesForUsd(): LiveData<List<ExchangeRates>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<Currency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeRates: List<ExchangeRates>)

    @Query("DELETE FROM Currency")
    suspend fun deleteAllCurrencies()

    @Query("DELETE FROM ExchangeRate")
    suspend fun deleteAllExchangeRates()

    @Transaction
    suspend fun updateCurrencies(currencies: List<Currency>) {
        deleteAllCurrencies()
        insertCurrencies(currencies)
    }

    @Transaction
    suspend fun updateExchangeRates(exchangeRates: List<ExchangeRates>) {
        deleteAllExchangeRates()
        insertExchangeRates(exchangeRates)
    }
}