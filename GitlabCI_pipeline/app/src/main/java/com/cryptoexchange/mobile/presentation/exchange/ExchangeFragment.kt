package com.cryptoexchange.mobile.presentation.exchange

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentExchangeBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.goneUnless
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.newCustomTab
import com.cryptoexchange.mobile.extensions.setDisablingClickListener
import com.cryptoexchange.mobile.presentation.exchange.filters.CurrencyChooserFragment
import com.cryptoexchange.mobile.presentation.exchange.filters.data.CurrencyChoosingInfo
import com.cryptoexchange.mobile.presentation.exchange.order.OrderPreviewFragment
import com.cryptoexchange.mobile.presentation.exchange.order.OrderPreviewInfo
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ExchangeFragment : BaseFragment(), ExchangeView {

    lateinit var binding: FragmentExchangeBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: ExchangePresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var givenTextWatcher: TextWatcher? = null
    private var receivevTextWatcher: TextWatcher? = null
    private var priceTextWatcher: TextWatcher? = null

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAndAddBackStackSaveStateObserver()
    }

    override fun setupUI() {
        with(binding) {
            initAndAddTextWatchers()

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.bell -> {
                        // TODO handle click on notifications icon in future.
                        true
                    }
                    else -> false
                }
            }

            exchangeTypeLayout.also {
                it.youWillGiveLayout.youWillGiveInput.maxInputLength = CURRENCY_INPUT_MAX_LENGTH
                it.youWillReceiveLayout.youWillReceiveInput.maxInputLength =
                    CURRENCY_INPUT_MAX_LENGTH
            }

            givingCurrencyDropDown().setOnClickListener {
                presenter.onGivenCurrencyClicked()
            }
            receivingCurrencyDropDown().setOnClickListener {
                presenter.onReceivedCurrencyClicked()
            }

            orderButton.setDisablingClickListener {
                presenter.onOrderButtonClicked()
            }

            reverseButton().setDisablingClickListener {
                presenter.onReverseButtonClicked()
            }
        }
    }

    override fun setupOrderTypeTab(types: Array<TransactionType>, tabGravity: Int) {
        with(binding.transactionTabLayout) {
            repeat(types.size) {
                val tabLabel = getString(types[it].label)
                View.inflate(context, R.layout.item_tab_exchange_custom_view, null)
                    .also { customView ->
                        customView.findViewById<AppCompatTextView>(R.id.textView).text = tabLabel
                        addTab(
                            newCustomTab(customView, tabGravity).apply {
                                tag = types[it]
                            }
                        )
                    }
            }

            addOnTabSelectedListener(
                OnTransactionTypeTabSelectedListener {
                    presenter.switchTransaction(it)
                }
            )
            val firstTab = this.getChildAt(0)
            if (firstTab is LinearLayout) {
                firstTab.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                val drawable = GradientDrawable()
                drawable.setColor(resources.getColor(R.color.gray))
                drawable.setSize(1, 1)
                firstTab.dividerDrawable = drawable
            }
        }
    }

    override fun switchTransactionType(transactionType: TransactionType) {
        with(binding) {
            transactionTabLayout.apply {
                val tabToSelect = getTabAt(transactionType.tabPosition)
                selectTab(tabToSelect)
            }
        }
    }

    override fun changePriceFieldVisibility(isLimit: Boolean) {
        priceInput().root.goneUnless(isLimit)
    }

    override fun navigateToConfirmation(info: OrderPreviewInfo) {
        navigate(
            R.id.action_exchangeDestination_to_orderPreviewFragment,
            OrderPreviewFragment.getBundle(info)
        )
    }

    override fun transactionButtonEnabled(isEnabled: Boolean) {
        binding.orderButton.isEnabled = isEnabled
    }

    override fun updateGivenAmount(convertedValue: String) {
        givenInputLayout().apply {
            if (!youWillGiveEditText.isFocused) {
                youWillGiveEditText.setText(convertedValue)
            }
        }
    }

    override fun updateReceivedAmount(convertedValue: String) {
        receivedInputLayout().apply {
            if (!youWillReceiveEditText.isFocused) {
                youWillReceiveEditText.setText(convertedValue)
            }
        }
    }

    override fun navigateToCurrencyChoosing(info: CurrencyChoosingInfo) {
        navigate(
            R.id.action_exchangeDestination_to_currencyChooserFragment,
            CurrencyChooserFragment.getBundle(info)
        )
    }

    override fun setGivenCurrency(currencyName: String, icon: Drawable?) {
        with(givingCurrencyDropDown()) {
            text = currencyName
            val arrow = ContextCompat.getDrawable(context, R.drawable.ic_drop_down_arrow_closed)
            setCompoundDrawablesWithIntrinsicBounds(icon, null, arrow, null)
        }
    }

    override fun setReceivedCurrency(currencyName: String, icon: Drawable?) {
        with(receivingCurrencyDropDown()) {
            text = currencyName
            val arrow = ContextCompat.getDrawable(context, R.drawable.ic_drop_down_arrow_closed)
            setCompoundDrawablesWithIntrinsicBounds(icon, null, arrow, null)
        }
    }

    override fun updateConvertingTip(isVisible: Boolean, text: String) {
        with(binding.convertedExchangeTextView) {
            this.text = text
            this.isVisible = isVisible
        }
    }

    override fun updateCommissionTip(isVisible: Boolean, text: String) {
        with(binding.feeTextView) {
            this.text = text
            this.isVisible = isVisible
        }
    }

    override fun showTooBigAmountError(message: String) {
        binding.exchangeTypeLayout.youWillGiveLayout.youWillGiveInput.error = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeTextWatchers()
    }

    private fun initAndAddBackStackSaveStateObserver() {
        val backStackSaveStateObserver = BackStackSaveStateObserver(
            this@ExchangeFragment,
            presenter::onCurrencyChosen
        )
        lifecycle.addObserver(backStackSaveStateObserver)
    }

    private fun initAndAddTextWatchers() {
        with(givenInputLayout().youWillGiveEditText) {
            givenTextWatcher = doOnTextChanged { text, _, _, _ ->
                presenter.onGivenInputChanged(text?.toString() ?: "")
            }
        }

        with(receivedInputLayout().youWillReceiveEditText) {
            receivevTextWatcher = doOnTextChanged { text, _, _, _ ->
                presenter.onReceivedInputChanged(text?.toString() ?: "")
            }
        }

        with(priceInput().priceEditText) {
            priceTextWatcher = doOnTextChanged { text, _, _, _ ->
                presenter.onPriceInputChanged(text)
            }
        }
    }

    private fun removeTextWatchers() {
        priceInput().priceEditText.removeTextChangedListener(priceTextWatcher)
        givenInputLayout().youWillGiveEditText.removeTextChangedListener(givenTextWatcher)
        receivedInputLayout().youWillReceiveEditText.removeTextChangedListener(receivevTextWatcher)
        givenTextWatcher = null
        priceTextWatcher = null
        receivevTextWatcher = null
    }

    private fun givenInputLayout() = binding.exchangeTypeLayout.youWillGiveLayout

    private fun receivedInputLayout() = binding.exchangeTypeLayout.youWillReceiveLayout

    private fun priceInput() = binding.limitTransactionPriceLayout

    private fun reverseButton() = binding.exchangeTypeLayout.reverseButton

    private fun givingCurrencyDropDown() = givenInputLayout().youWillGiveCurrencyDropDown

    private fun receivingCurrencyDropDown() = receivedInputLayout().youWillReceiveCurrencyDropDown

    companion object {
        private const val CURRENCY_INPUT_MAX_LENGTH = 10
    }
}
