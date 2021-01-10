package com.fabinpaul.currencydata.source.remote

import com.fabinpaul.currencydata.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerService {

    @GET("list")
    fun listCurrencies(@Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY): Call<ResponseBody>?

    @GET("live")
    fun getDollarCurrencyConversion(@Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY): Call<ResponseBody>?
}