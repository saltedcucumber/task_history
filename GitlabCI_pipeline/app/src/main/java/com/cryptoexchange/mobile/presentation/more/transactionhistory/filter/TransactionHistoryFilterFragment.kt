package com.cryptoexchange.mobile.presentation.more.transactionhistory.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.navigation.fragment.findNavController
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTransactionHistoryFilterBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.*
import com.google.android.material.datepicker.MaterialDatePicker
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

private typealias Binding = FragmentTransactionHistoryFilterBinding

class TransactionHistoryFilterFragment : BaseFragment(), TransactionHistoryFilterView {

    private lateinit var binding: Binding

    @Inject
    @InjectPresenter
    lateinit var presenter: TransactionHistoryFilterPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var rangeDatePicker: MaterialDatePicker<Pair<Long, Long>>? = null

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        // FIXME Put to presenters constructor
        val settings = arguments?.getParcelable<TransactionFilterSettings>(TRANSACTION_FILTER_KEY)
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
                    paymentTypeInputLayout.paymentTypeACTextView.value,
                    currencyInputLayout.currencyACTextView.value
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
            paymentTypeInputLayout.paymentTypeACTextView.text.clear()
            currencyInputLayout.currencyACTextView.text.clear()
        }
    }

    override fun applyFilter(settings: TransactionFilterSettings?) {
        findNavController().apply {
            previousBackStackEntry?.savedStateHandle?.apply {
                set(TRANSACTION_FILTER_KEY, settings)
            }
            popBackStack()
        }
    }

    override fun setupPreviousFilterSettings(settings: TransactionFilterSettings?) {
        with(binding) {
            settings?.let {
                datePickerInputLayout.datePickerEditText.setText(it.rangeDate)
                paymentTypeInputLayout.paymentTypeACTextView.setText(it.type, false)
                currencyInputLayout.currencyACTextView.setText(it.currency, false)
            }
        }
    }

    override fun setupPaymentTypeSelector(
        paymentTypes: List<String>
    ) {
        binding.paymentTypeInputLayout.paymentTypeACTextView.setupDropdownMenu(paymentTypes)
    }

    override fun setupCurrencySelector(
        currencies: List<String>
    ) {
        binding.currencyInputLayout.currencyACTextView.setupDropdownMenu(currencies)
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {

        const val TRANSACTION_FILTER_KEY = "TRANSACTION_FILTER_KEY"

        fun getBundle(
            settings: TransactionFilterSettings
        ): Bundle {
            return Bundle().apply {
                putParcelable(TRANSACTION_FILTER_KEY, settings)
            }
        }
    }
}
