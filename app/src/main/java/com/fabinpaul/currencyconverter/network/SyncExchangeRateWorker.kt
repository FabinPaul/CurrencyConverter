package com.fabinpaul.currencyconverter.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fabinpaul.currencyconverter.di.InjectDependency
import com.fabinpaul.currencyconverter.log.L
import com.fabinpaul.currencydata.model.Output
import kotlinx.coroutines.withContext

class SyncExchangeRateWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        L.d(SyncExchangeRateWorker::class.java, "Started Exchange Rate Sync")
        val refreshOutput =
            InjectDependency.getCurrencyExchangeRepository(appContext).refreshRates()
        return withContext(AppDispatcher.Main) {
            if (refreshOutput is Output.Success) {
                Result.success()
            } else {
                Result.failure()
            }
        }
    }
}