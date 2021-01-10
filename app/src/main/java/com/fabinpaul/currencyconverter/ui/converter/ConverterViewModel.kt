package com.fabinpaul.currencyconverter.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabinpaul.currencyconverter.network.AppDispatcher
import com.fabinpaul.currencyconverter.network.CoroutineDispatcherProvider
import com.fabinpaul.currencydata.model.Output
import com.fabinpaul.currencydata.source.CurrencyExchangeRepository
import kotlinx.coroutines.*
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

    private val _listOfCurrencyValues =
        MutableLiveData<List<String>>().apply { value = mutableListOf() }
    val listOfCurrencyValues: LiveData<List<String>>
        get() = _listOfCurrencyValues

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private var selectedCurrencyCode: String? = null

    init {
        _dataLoading.value = true
        launch(appDispatcher.IO) {
            currencyExchangeRepository.refreshRates()
            val supportCurrenciesOutput = currencyExchangeRepository.getSupportedCurrencyCodes()
            if (supportCurrenciesOutput is Output.Success) {
                withContext(appDispatcher.Main) {
                    _dataLoading.value = false
                    _listOfCurrencyValues.value = supportCurrenciesOutput.data
                }
            }
        }
    }

    private fun updateExchangeRates(selectedCurrencyCode: String?, amountDouble: Double?) {
        if (amountDouble != null && !selectedCurrencyCode.isNullOrEmpty()) {
            _dataLoading.value = true
            launch(appDispatcher.IO) {
                val exchangeRatesOutput =
                    currencyExchangeRepository.getExchangeRates(selectedCurrencyCode)
                val convertedViewModelList = ArrayList<ConvertedViewModel>()
                if (exchangeRatesOutput is Output.Success) {
                    exchangeRatesOutput.data.forEach {
                        convertedViewModelList.add(
                            ConvertedViewModel(
                                currencyExchangeRepository,
                                it.currencyCodeTo,
                                it.rate,
                                amountDouble,
                                appDispatcher
                            )
                        )
                    }
                    withContext(appDispatcher.Main) {
                        _dataLoading.value = false
                        _listOfConverterValues.value = convertedViewModelList
                    }
                }
            }
        }
    }

    fun updateSelectedCurrency(selectedCurrencyCode: String) {
        this.selectedCurrencyCode = selectedCurrencyCode
        updateAmount(amount.value)
    }

    fun updateAmount(amountString: String?) {
        val amountDouble = amountString?.toDoubleOrNull()
        updateExchangeRates(selectedCurrencyCode, amountDouble)
    }

}