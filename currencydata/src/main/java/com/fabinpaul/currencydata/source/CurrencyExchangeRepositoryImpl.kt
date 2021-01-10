package com.fabinpaul.currencydata.source

import com.fabinpaul.currencydata.model.ExchangeRates
import com.fabinpaul.currencydata.model.Output
import java.lang.IllegalArgumentException

class CurrencyExchangeRepositoryImpl private constructor(
    private val currencyExchangeLocalDataSource: CurrencyExchangeDataSource,
    private val currencyExchangeRemoteDataSource: CurrencyExchangeDataSource
) : CurrencyExchangeRepository {

    private var currencyInfo: LinkedHashMap<String, String>? = null
    private var currencyMap: LinkedHashMap<String, Double>? = null
    private var exchangeRatesUsd: List<ExchangeRates>? = null

    override suspend fun getSupportedCurrencyCodes(): Output<List<String>> {
        val currencyInfoLocal = this.currencyInfo
        return if (currencyInfoLocal == null) {
            val currenciesOutput = currencyExchangeLocalDataSource.getListOfCurrencies()
            if (currenciesOutput is Output.Success) {
                currencyInfo = LinkedHashMap()
                val listOfCurrencyCodes = ArrayList<String>()
                currenciesOutput.data.forEach {
                    listOfCurrencyCodes.add(it.currencyCode)
                    currencyInfo?.put(it.currencyCode, it.country)
                }
                Output.Success(listOfCurrencyCodes)
            } else {
                Output.Error((currenciesOutput as Output.Error).exception)
            }
        } else {
            Output.Success(ArrayList(currencyInfoLocal.keys))
        }
    }

    override suspend fun getCurrencyInfo(currencyCode: String): Output<String> {
        val currencyInfoLocal = this.currencyInfo
        return if (currencyInfoLocal == null) {
            val currenciesOutput = currencyExchangeLocalDataSource.getListOfCurrencies()
            if (currenciesOutput is Output.Success) {
                currencyInfo = LinkedHashMap()
                val listOfCurrencyCodes = ArrayList<String>()
                currenciesOutput.data.forEach {
                    listOfCurrencyCodes.add(it.currencyCode)
                    currencyInfo?.put(it.currencyCode, it.country)
                }
                Output.Success(currencyInfo?.get(currencyCode)!!)
            } else {
                Output.Error((currenciesOutput as Output.Error).exception)
            }
        } else {
            if (currencyInfoLocal.containsKey(currencyCode)) {
                Output.Success(currencyInfoLocal[currencyCode]!!)
            } else {
                Output.Error(IllegalArgumentException("Unsupported currency"))
            }
        }
    }

    override suspend fun getExchangeRates(currencyCode: String): Output<List<ExchangeRates>> {
        val rateWrtUsdOutput = if (currencyMap == null) {
            val exchangeRatesUsdOutput = currencyExchangeLocalDataSource.getExchangeRatesForUsd()
            if (exchangeRatesUsdOutput is Output.Success) {
                currencyMap = LinkedHashMap()
                exchangeRatesUsd = exchangeRatesUsdOutput.data
                exchangeRatesUsdOutput.data.forEach {
                    currencyMap?.put(it.currencyCodeTo, it.rate)
                }
                Output.Success(currencyMap?.get(currencyCode))
            } else {
                Output.Error((exchangeRatesUsdOutput as Output.Error).exception)
            }
        } else if (currencyMap?.containsKey(currencyCode) == true) {
            Output.Success(currencyMap?.get(currencyCode))
        } else {
            Output.Error(IllegalArgumentException("Unsupported currency"))
        }

        return if (rateWrtUsdOutput is Output.Success) {
            if (currencyCode.equals("USD") && exchangeRatesUsd != null) {
                Output.Success(exchangeRatesUsd!!)
            } else {
                val rateWrtUsd = rateWrtUsdOutput.data
                val exchangeRates = ArrayList<ExchangeRates>()
                exchangeRatesUsd?.forEach {
                    exchangeRates.add(
                        ExchangeRates(currencyCode, it.currencyCodeTo, it.rate / rateWrtUsd!!)
                    )
                }
                Output.Success(exchangeRates)
            }
        } else {
            Output.Error((rateWrtUsdOutput as Output.Error).exception)
        }
    }

    override suspend fun refreshRates(): Output<Boolean> {
        val currenciesRemoteUpdateOutput = currencyExchangeRemoteDataSource.getListOfCurrencies()
        return if (currenciesRemoteUpdateOutput is Output.Success) {
            currencyExchangeLocalDataSource.updateCurrencies(currenciesRemoteUpdateOutput.data)
            currencyInfo = LinkedHashMap()
            currenciesRemoteUpdateOutput.data.forEach {
                currencyInfo?.put(it.currencyCode, it.country)
            }
            val exchangeRatesRemoteUpdateOutput =
                currencyExchangeRemoteDataSource.getExchangeRatesForUsd()
            if (exchangeRatesRemoteUpdateOutput is Output.Success) {
                currencyExchangeLocalDataSource.updateExchangeRates(exchangeRatesRemoteUpdateOutput.data)
                exchangeRatesUsd = exchangeRatesRemoteUpdateOutput.data
                currencyMap = LinkedHashMap()
                exchangeRatesRemoteUpdateOutput.data.forEach {
                    currencyMap?.put(it.currencyCodeTo, it.rate)
                }
                Output.Success(true)
            } else {
                Output.Error((exchangeRatesRemoteUpdateOutput as Output.Error).exception)
            }
        } else {
            Output.Error((currenciesRemoteUpdateOutput as Output.Error).exception)
        }
    }

    companion object {

        private var INSTANCE: CurrencyExchangeRepository? = null

        @JvmStatic
        fun getInstance(
            currencyExchangeLocalDataSource: CurrencyExchangeDataSource,
            currencyExchangeRemoteDataSource: CurrencyExchangeDataSource
        ) = INSTANCE ?: synchronized(CurrencyExchangeRepositoryImpl::class.java) {
            INSTANCE ?: CurrencyExchangeRepositoryImpl(
                currencyExchangeLocalDataSource,
                currencyExchangeRemoteDataSource
            ).also {
                INSTANCE = it
            }
        }
    }
}