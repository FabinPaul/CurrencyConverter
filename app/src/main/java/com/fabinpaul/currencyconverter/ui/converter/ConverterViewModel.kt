package com.fabinpaul.currencyconverter.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    val amount = MutableLiveData<String>()

    private val _listOfConverterValues =
        MutableLiveData<List<ConvertedViewModel>>().apply { value = mutableListOf() }
    val listOfConverterValues: LiveData<List<ConvertedViewModel>>
        get() = _listOfConverterValues

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

}