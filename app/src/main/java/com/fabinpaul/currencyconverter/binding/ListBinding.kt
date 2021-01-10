package com.fabinpaul.currencyconverter.binding

import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.fabinpaul.currencyconverter.adapter.SimpleConverterAdapter

object ListBinding {

    @BindingAdapter(value = ["listOfViewModels", "itemLayout"], requireAll = true)
    @JvmStatic
    fun <T : ViewModel> setSimpleAdapter(
        recyclerView: RecyclerView,
        listOfViewModels: List<T>?,
        @LayoutRes itemLayoutRes: Int
    ) {
        listOfViewModels?.let {
            val adapter = SimpleConverterAdapter(listOfViewModels, itemLayoutRes)
            recyclerView.adapter = adapter
        }
    }
}