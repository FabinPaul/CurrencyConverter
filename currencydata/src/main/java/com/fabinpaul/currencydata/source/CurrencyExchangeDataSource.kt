package com.fabinpaul.currencydata.source

import androidx.lifecycle.LiveData
import com.fabinpaul.currencydata.model.Currency
import com.fabinpaul.currencydata.model.ExchangeRates
import com.fabinpaul.currencydata.model.Output

interface CurrencyExchangeDataSource {

    suspend fun getListOfCurrencies(): Output<List<Currency>>

    suspend fun getExchangeRatesForUsd(): Output<List<ExchangeRates>>

    fun observeExchangeRatesForUsd(): LiveData<Output<List<ExchangeRates>>>

    suspend fun updateCurrencies(currencies: List<Currency>)

    suspend fun updateExchangeRates(exchangeRates: List<ExchangeRates>)
}