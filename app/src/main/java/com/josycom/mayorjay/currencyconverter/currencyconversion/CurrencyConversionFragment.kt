package com.josycom.mayorjay.currencyconverter.currencyconversion

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.currencyconverter.R
import com.josycom.mayorjay.currencyconverter.common.domain.model.Currency
import com.josycom.mayorjay.currencyconverter.common.util.Constants
import com.josycom.mayorjay.currencyconverter.common.util.Resource
import com.josycom.mayorjay.currencyconverter.common.util.dpToPx
import com.josycom.mayorjay.currencyconverter.common.util.isEmptyOrNull
import com.josycom.mayorjay.currencyconverter.common.util.showToast
import com.josycom.mayorjay.currencyconverter.databinding.FragmentCurrencyConversionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyConversionFragment : Fragment() {

    private var _binding: FragmentCurrencyConversionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrencyConversionViewModel by viewModels()
    @Inject
    lateinit var rateAdapter: RateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentCurrencyConversionBinding.inflate(layoutInflater).apply {
            _binding = this
            return this.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrencies()
        getRates() //Display the rates for each currency instead of showing a blank screen
        setupRecyclerView()
        setupListeners()
        observeLiveData()
    }

    private fun setupRecyclerView() {
        binding.rvRates.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(createItemDecoration())
            adapter = rateAdapter
        }
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val spacing = 4F.dpToPx()
                outRect.apply {
                    left = spacing
                    top = spacing
                    right = spacing
                    bottom = spacing
                }
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            etAmount.addTextChangedListener(afterTextChanged = {
                if (it.toString().isEmptyOrNull()) {
                    getRates() //Display the rates for each currency instead of showing the last converted amount
                    viewModel.amountInputted = Constants.EMPTY_STRING_VALUE
                    return@addTextChangedListener
                }
                viewModel.amountInputted = it.toString()
            })

            spCurrencies.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.currencySelected = p0?.getItemAtPosition(p2) as String?
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            btConvert.setOnClickListener {
                if (!viewModel.validateInputs()) return@setOnClickListener
                convert(viewModel.amountInputted?.toDouble(), viewModel.currencySelected)
            }

            ivStatus.setOnClickListener {
                getCurrencies()
                getRates()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.getCurrenciesLiveData().observe(viewLifecycleOwner) { resource ->
            resource.data?.let { populateSpinner(it) }

            when (resource) {
                is Resource.Loading -> {
                    binding.spCurrencies.isEnabled = false
                }

                is Resource.Success -> {
                    binding.spCurrencies.isEnabled = true
                }

                is Resource.Error -> { }
            }
        }

        viewModel.getRatesLiveData().observe(viewLifecycleOwner) { resource ->
            rateAdapter.submitList(resource.data)

            when (resource) {
                is Resource.Loading -> {
                    binding.tvStatus.isVisible = false
                    binding.ivStatus.isVisible = true
                    binding.rvRates.isVisible = false
                    binding.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.loading_animation, null))
                }

                is Resource.Success -> {
                    binding.rvRates.isVisible = true
                    binding.tvStatus.isVisible = false
                    binding.ivStatus.isVisible = false
                }

                is Resource.Error -> {
                    binding.tvStatus.isVisible = true
                    val msg = resource.error?.message ?: Constants.EMPTY_STRING_VALUE
                    binding.tvStatus.text = if (msg.contains("403")) Constants.APP_ID_ERROR_MSG else getString(R.string.network_error_message, msg)
                    binding.ivStatus.isVisible = true
                    binding.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_connection_error, null))
                }
            }
        }

        viewModel.getErrorMsgLiveData().observe(viewLifecycleOwner) { msg ->
            if (msg.isEmptyOrNull()) return@observe
            msg.showToast(requireContext())
            viewModel.setErrorMsgLiveDataValue(Constants.EMPTY_STRING_VALUE)
        }
    }

    private fun populateSpinner(currencies: List<Currency>) {
        val items = viewModel.getSpinnerItems(currencies)
        binding.spCurrencies.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun getCurrencies() = viewModel.getCurrencies()

    private fun getRates() = viewModel.getRates()

    private fun convert(amount: Double?, fromCurrency: String?) {
        viewModel.convert(
            amount ?: Constants.EMPTY_DOUBLE_VALUE,
            fromCurrency ?: Constants.EMPTY_STRING_VALUE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}