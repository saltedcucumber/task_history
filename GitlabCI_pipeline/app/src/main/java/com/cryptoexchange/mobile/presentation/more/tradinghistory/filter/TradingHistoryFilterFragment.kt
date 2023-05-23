package com.cryptoexchange.mobile.presentation.more.tradinghistory.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.navigation.fragment.findNavController
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTradingHistoryFilterBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.*
import com.google.android.material.datepicker.MaterialDatePicker
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

private typealias Binding = FragmentTradingHistoryFilterBinding

class TradingHistoryFilterFragment : BaseFragment(), TradingHistoryFilterView {

    private lateinit var binding: Binding

    @Inject
    @InjectPresenter
    lateinit var presenter: TradingHistoryFilterPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var rangeDatePicker: MaterialDatePicker<Pair<Long, Long>>? = null

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        // FIXME Put to presenters constructor
        val settings = arguments?.getParcelable<TradingFilterSettings>(TRADING_FILTER_KEY)
        presenter.setData(settings)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.reset -> {
                        presenter.onResetClicked()
                        true
                    }
                    else -> false
                }
            }
            buttonBack.setOnClickListener { presenter.onBackPressed() }

            rangeDatePicker = MaterialDatePicker
                .Builder.dateRangePicker()
                .build().also { datePicker ->
                    datePicker.addOnPositiveButtonClickListener {
                        datePickerInputLayout.datePickerEditText.setText(
                            it.toRange("-", DD_MM_YYYY)
                        )
                    }
                }

            datePickerInputLayout.root.setEndIconOnClickListener {
                presenter.onDatePickerClicked()
            }

            applyButton.setOnClickListener {
                presenter.onApplyFilterClicked(
                    datePickerInputLayout.datePickerEditText.value,
                    transactionTypeInputLayout.transactionTypeACTextView.value,
                    exchangeTypeInputLayout.exchangeTypeACTextView.value
                )
            }
        }
    }

    override fun showDatePicker() {
        rangeDatePicker?.show(childFragmentManager, MaterialDatePicker::class.java.simpleName)
    }

    override fun clearFields() {
        with(binding) {
            datePickerInputLayout.datePickerEditText.text?.clear()
            transactionTypeInputLayout.transactionTypeACTextView.text.clear()
            exchangeTypeInputLayout.exchangeTypeACTextView.text.clear()
        }
    }

    override fun applyFilter(settings: TradingFilterSettings?) {
        findNavController().apply {
            previousBackStackEntry?.savedStateHandle?.apply {
                set(TRADING_FILTER_KEY, settings)
            }
            popBackStack()
        }
    }

    override fun setupPreviousFilterSettings(settings: TradingFilterSettings?) {
        with(binding) {
            settings?.let {
                datePickerInputLayout.datePickerEditText.setText(it.rangeDate)
                transactionTypeInputLayout.transactionTypeACTextView.setText(it.type, false)
                exchangeTypeInputLayout.exchangeTypeACTextView.setText(it.side, false)
            }
        }
    }

    override fun setupTransactionTypeSelector(
        transactionTypes: List<String>
    ) {
        binding.transactionTypeInputLayout.transactionTypeACTextView.setupDropdownMenu(
            transactionTypes
        )
    }

    override fun setupExchangeTypeSelector(
        exchangesTypes: List<String>
    ) {
        binding.exchangeTypeInputLayout.exchangeTypeACTextView.setupDropdownMenu(
            exchangesTypes
        )
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {
        const val TRADING_FILTER_KEY = "TRADING_FILTER_KEY"

        fun getBundle(
            settings: TradingFilterSettings
        ): Bundle {
            return Bundle().apply {
                putParcelable(TRADING_FILTER_KEY, settings)
            }
        }
    }
}
