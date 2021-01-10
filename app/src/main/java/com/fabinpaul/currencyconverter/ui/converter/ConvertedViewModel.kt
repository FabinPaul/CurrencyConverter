package com.fabinpaul.currencyconverter.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabinpaul.currencyconverter.network.AppDispatcher
import com.fabinpaul.currencyconverter.network.CoroutineDispatcherProvider
import com.fabinpaul.currencydata.model.Output
import com.fabinpaul.currencydata.source.CurrencyExchangeRepository
import kotlinx.coroutines.*
import java.text.DecimalFormat
import kotlin.coroutines.CoroutineContext

class ConvertedViewModel(
    private val currencyExchangeRepository: CurrencyExchangeRepository,
    currencyCode: String,
    rate: Double,
    amount: Double,
    private val appDispatcher: CoroutineDispatcherProvider = AppDispatcher
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount

    private val _currencyInfo = MutableLiveData<String>()
    val currencyInfo: LiveData<String>
        get() = _currencyInfo

    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String>
        get() = _currency

    private val _dataLoading = MutableLiveData<Boolean>(true)
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val decimalFormat = DecimalFormat("#.##")

    init {
        launch(appDispatcher.IO) {
            val amountString = decimalFormat.format(amount * rate)
            val currencyInfoOutput = currencyExchangeRepository.getCurrencyInfo(currencyCode)
            withContext(appDispatcher.Main) {
                _dataLoading.value = false
                if (currencyInfoOutput is Output.Success) {
                    _currencyInfo.value = currencyInfoOutput.data
                }
                _amount.value = amountString
                _currency.value = currencyCode
            }
        }
    }
}