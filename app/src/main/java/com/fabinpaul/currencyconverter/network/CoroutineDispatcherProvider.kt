package com.fabinpaul.currencyconverter.network

import kotlin.coroutines.CoroutineContext

interface CoroutineDispatcherProvider {
    val Main: CoroutineContext

    val IO: CoroutineContext
}