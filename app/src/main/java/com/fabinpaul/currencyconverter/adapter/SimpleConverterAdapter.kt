package com.fabinpaul.currencyconverter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.fabinpaul.currencyconverter.BR
import java.lang.IllegalStateException

class SimpleConverterAdapter<T : ViewModel>(
    private val listOfItemViewModels: List<T>,
    @LayoutRes private val layoutRes: Int
) : RecyclerView.Adapter<SimpleConverterAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder =
        RecyclerViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutRes, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val bindingStatus = holder.binding.setVariable(BR.viewModel, listOfItemViewModels[position])
        if (!bindingStatus) {
            throw IllegalStateException("Binding ${holder.binding} variable name should be 'viewModel'")
        }
    }

    override fun getItemCount(): Int = listOfItemViewModels.size

    class RecyclerViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}