package com.fabinpaul.currencyconverter.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabinpaul.currencyconverter.network.AppDispatcher
import com.fabinpaul.currencyconverter.network.CoroutineDispatcherProvider
import com.fabinpaul.currencydata.source.CurrencyExchangeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ConverterViewModel(
    private val currencyExchangeRepository: CurrencyExchangeRepository,
    private val appDispatcher: CoroutineDispatcherProvider = AppDispatcher
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    val amount = MutableLiveData<String>()

    private val _listOfConverterValues =
        MutableLiveData<List<ConvertedViewModel>>().apply { value = mutableListOf() }
    val listOfConverterValues: LiveData<List<ConvertedViewModel>>
        get() = _listOfConverterValues

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    init {
        launch(appDispatcher.IO) {
            currencyExchangeRepository.refreshRates()
        }
    }

}