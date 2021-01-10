package com.fabinpaul.currencyconverter.di

import android.content.Context
import com.fabinpaul.currencydata.CurrencyExchangeDatabase
import com.fabinpaul.currencydata.source.CurrencyExchangeRepository
import com.fabinpaul.currencydata.source.CurrencyExchangeRepositoryImpl
import com.fabinpaul.currencydata.source.local.CurrencyExchangeLocalDataSource
import com.fabinpaul.currencydata.source.remote.CurrencyLayerRemoteDataSource

object InjectDependency {

    fun getCurrencyExchangeRepository(context: Context): CurrencyExchangeRepository =
        CurrencyExchangeRepositoryImpl.getInstance(
            CurrencyExchangeLocalDataSource.getInstance(
                CurrencyExchangeDatabase.getAppDatabase(context)
                    .currencyExchangeDao()
            ),
            CurrencyLayerRemoteDataSource.getInstance()
        )
}