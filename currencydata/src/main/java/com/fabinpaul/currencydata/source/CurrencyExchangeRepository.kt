package com.fabinpaul.currencydata.source

import com.fabinpaul.currencydata.model.ExchangeRates
import com.fabinpaul.currencydata.model.Output

interface CurrencyExchangeRepository {

    suspend fun getSupportedCurrencyCodes(): Output<List<String>>

    suspend fun getCurrencyInfo(currencyCode: String): Output<String>

    suspend fun getExchangeRates(currencyCode: String): Output<List<ExchangeRates>>

    suspend fun refreshRates(): Output<Boolean>

}