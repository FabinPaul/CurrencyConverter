package com.fabinpaul.currencyconverter.network

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object AppDispatcher : CoroutineDispatcherProvider {

    override val Main: CoroutineContext = Dispatchers.Main

    override val IO: CoroutineContext = Dispatchers.IO
}