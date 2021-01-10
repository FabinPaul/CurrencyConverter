package com.fabinpaul.currencydata.source.remote

import androidx.lifecycle.LiveData
import com.fabinpaul.currencydata.BuildConfig
import com.fabinpaul.currencydata.model.Currency
import com.fabinpaul.currencydata.model.ExchangeRates
import com.fabinpaul.currencydata.model.Output
import com.fabinpaul.currencydata.source.CurrencyExchangeDataSource
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CurrencyLayerRemoteDataSource private constructor() : CurrencyExchangeDataSource {

    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BuildConfig.CURRENCY_LAYER_URL)
        .build()

    override suspend fun getListOfCurrencies(): Output<List<Currency>> {
        return suspendCoroutine {
            val response = retrofit.create(CurrencyLayerService::class.java).listCurrencies()
            if (response != null) {
                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val jsonResponse = JSONObject(response.body()?.string() ?: "{}")
                        val currencyList = ArrayList<Currency>()
                        val currenciesJson = jsonResponse.optJSONObject("currencies")
                        val currencyCodesJson: JSONArray? = currenciesJson?.names()
                        val len = currencyCodesJson?.length() ?: 0
                        for (i in 0 until len) {
                            val currencyCode = currencyCodesJson?.optString(i) ?: ""
                            val currencyInfo = currenciesJson?.optString(currencyCode) ?: ""
                            currencyList.add(Currency(currencyInfo, currencyCode))
                        }
                        it.resume(Output.Success(currencyList))
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        it.resume(Output.Error(Exception(t)))
                    }

                })
            } else {
                it.resume(Output.Error(NullPointerException("Get Currency List Service returned null")))
            }
        }
    }

    override suspend fun getExchangeRatesForUsd(): Output<List<ExchangeRates>> {
        return suspendCoroutine {
            val response =
                retrofit.create(CurrencyLayerService::class.java).getDollarCurrencyConversion()
            if (response != null) {
                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val jsonResponse = JSONObject(response.body()?.string() ?: "{}")
                        val exchangeRateList = ArrayList<ExchangeRates>()
                        val sourceCurrencyCode = jsonResponse.optString("source")
                        val quotesJson = jsonResponse.optJSONObject("quotes")
                        val quotesCodesJson: JSONArray? = quotesJson?.names()
                        val len = quotesCodesJson?.length() ?: 0
                        for (i in 0 until len) {
                            val quoteCode = quotesCodesJson?.optString(i) ?: ""
                            val currencyCodeTo = quoteCode.substring(sourceCurrencyCode.length)
                            val rate = quotesJson?.optDouble(quoteCode) ?: 0.0
                            exchangeRateList.add(
                                ExchangeRates(
                                    sourceCurrencyCode,
                                    currencyCodeTo,
                                    rate
                                )
                            )
                        }
                        it.resume(Output.Success(exchangeRateList))
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        it.resume(Output.Error(Exception(t)))
                    }

                })
            } else {
                it.resume(Output.Error(NullPointerException("Get Currency List Service returned null")))
            }
        }
    }

    override fun observeExchangeRatesForUsd(): LiveData<Output<List<ExchangeRates>>> {
        throw UnsupportedOperationException("Currency only local DS support observe functionality")
    }

    override suspend fun updateCurrencies(currencies: List<Currency>) {
        throw UnsupportedOperationException("Updating currency is not supported by this DS")
    }

    override suspend fun updateExchangeRates(exchangeRates: List<ExchangeRates>) {
        throw UnsupportedOperationException("Updating Exchange Rates is not supported by this DS")
    }

    companion object {

        private var INSTANCE: CurrencyExchangeDataSource? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(CurrencyLayerRemoteDataSource::class.java) {
            INSTANCE ?: CurrencyLayerRemoteDataSource().also { INSTANCE = it }
        }
    }
}