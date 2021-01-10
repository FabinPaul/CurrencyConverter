package com.fabinpaul.currencydata.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.fabinpaul.currencydata.model.Currency
import com.fabinpaul.currencydata.model.ExchangeRates
import com.fabinpaul.currencydata.model.Output
import com.fabinpaul.currencydata.source.CurrencyExchangeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyExchangeLocalDataSource private constructor(
    private val currencyExchangeDao: CurrencyExchangeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyExchangeDataSource {

    override suspend fun getListOfCurrencies() = withContext(ioDispatcher) {
        return@withContext try {
            Output.Success(currencyExchangeDao.listCurrencies())
        } catch (e: Exception) {
            Output.Error(e)
        }
    }

    override suspend fun getExchangeRatesForUsd() = withContext(ioDispatcher) {
        return@withContext try {
            Output.Success(currencyExchangeDao.getExchangeRatesForUsd())
        } catch (e: Exception) {
            Output.Error(e)
        }
    }

    override fun observeExchangeRatesForUsd(): LiveData<Output<List<ExchangeRates>>> {
        return currencyExchangeDao.observeExchangeRatesForUsd().map {
            Output.Success(it)
        }
    }

    override suspend fun updateCurrencies(currencies: List<Currency>) {
        currencyExchangeDao.updateCurrencies(currencies)
    }

    override suspend fun updateExchangeRates(exchangeRates: List<ExchangeRates>) {
        currencyExchangeDao.updateExchangeRates(exchangeRates)
    }

    companion object {

        private var INSTANCE: CurrencyExchangeDataSource? = null

        @JvmStatic
        fun getInstance(currencyExchangeDao: CurrencyExchangeDao) =
            INSTANCE ?: synchronized(CurrencyExchangeLocalDataSource::class.java) {
                INSTANCE ?: CurrencyExchangeLocalDataSource(currencyExchangeDao).also {
                    INSTANCE = it
                }
            }
    }
}