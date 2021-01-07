package com.fabinpaul.currencyconverter.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConvertedViewModel : ViewModel() {

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount

    private val _currencyInfo = MutableLiveData<String>()
    val currencyInfo: LiveData<String>
        get() = _currencyInfo

    private val _currency = MutableLiveData<String>()
    val currency: LiveData<String>
        get() = _currency
}