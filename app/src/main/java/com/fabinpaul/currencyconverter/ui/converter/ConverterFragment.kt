package com.fabinpaul.currencyconverter.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fabinpaul.currencyconverter.R
import com.fabinpaul.currencyconverter.binding.getViewModel
import com.fabinpaul.currencyconverter.databinding.ConverterFragmentBinding
import com.fabinpaul.currencyconverter.di.InjectDependency
import com.fabinpaul.currencyconverter.network.SyncExchangeRateWorker
import java.util.concurrent.TimeUnit

class ConverterFragment : Fragment() {

    private var syncRequest: PeriodicWorkRequest? = null
    private var binding: ConverterFragmentBinding? = null
    private lateinit var viewModel: ConverterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel {
            ConverterViewModel(
                InjectDependency.getCurrencyExchangeRepository(requireContext())
            )
        }

        syncRequest =
            PeriodicWorkRequestBuilder<SyncExchangeRateWorker>(15, TimeUnit.MINUTES)
                .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = ConverterFragmentBinding.inflate(inflater, container, false)
        fragmentBinding.viewModel = viewModel
        fragmentBinding.lifecycleOwner = this
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.listOfCurrencyValues.observe(viewLifecycleOwner, {
            val dropDownAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_currency,
                it
            )
            binding?.includeConverterContent?.atvCurrencyDropDown?.setAdapter(dropDownAdapter)
        })

        viewModel.amount.observe(viewLifecycleOwner, {
            viewModel.updateAmount(it)
        })

        binding?.includeConverterContent?.atvCurrencyDropDown?.setOnItemClickListener { adapterView, _, pos, _ ->
            viewModel.updateSelectedCurrency(adapterView.getItemAtPosition(pos) as String)
        }
    }

    companion object {
        fun newInstance() = ConverterFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            val requestId = syncRequest?.id
            if (requestId != null) {
                WorkManager.getInstance(it).cancelWorkById(requestId)
            }
        }
    }

}