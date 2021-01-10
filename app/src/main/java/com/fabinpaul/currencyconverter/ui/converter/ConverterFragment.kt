package com.fabinpaul.currencyconverter.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fabinpaul.currencyconverter.R
import com.fabinpaul.currencyconverter.binding.ViewModelFactory
import com.fabinpaul.currencyconverter.binding.getViewModel
import com.fabinpaul.currencydata.CurrencyExchangeDatabase
import com.fabinpaul.currencydata.source.CurrencyExchangeRepositoryImpl
import com.fabinpaul.currencydata.source.local.CurrencyExchangeLocalDataSource
import com.fabinpaul.currencydata.source.remote.CurrencyLayerRemoteDataSource

class ConverterFragment : Fragment() {

    companion object {
        fun newInstance() = ConverterFragment()
    }

    private lateinit var viewModel: ConverterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.converter_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel {
            ConverterViewModel(
                CurrencyExchangeRepositoryImpl.getInstance(
                    CurrencyExchangeLocalDataSource.getInstance(
                        CurrencyExchangeDatabase.getAppDatabase(requireContext())
                            .currencyExchangeDao()
                    ),
                    CurrencyLayerRemoteDataSource.getInstance()
                )
            )
        }
    }

}