package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentWithdrawBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.*
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.WithdrawType
import com.cryptoexchange.source.extensions.getCurrencyIcon
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class WithdrawFragment : BaseFragment(), WithdrawView {

    private lateinit var binding: FragmentWithdrawBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: WithdrawPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        // FIXME Put to presenters constructor
        val depositInfo = tryToGetParcelable<WithdrawType>(KEY_WITHDRAW_TYPE)
        presenter.data(depositInfo)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonProceed.setOnClickListener {
                val memo = memoLayout.run {
                    if (memoInputLayout.isVisible) {
                        memoEditText.value
                    } else {
                        null
                    }
                }
                presenter.onProceedClicked(
                    amountString = amountLayout.amountEditText.value,
                    receiveAddress = receiveAddressLayout.receiveAddressEditText.value,
                    tfaCode = tfaCodeLayout.tfaCodeEditText.value,
                    memo = memo
                )
            }
        }
    }

    override fun showMemoInput() {
        binding.memoLayout.memoInputLayout.isVisible = true
    }

    @SuppressLint("SetTextI18n")
    override fun updateUIWithArguments(withdrawType: WithdrawType) {
        with(binding) {
            with(withdrawType) {
                val currencyShortName = wallet.currencyShortName
                tokenTextView.apply {
                    text = currencyShortName
                    // TODO update by backend value
                    val icon = getDrawable(getCurrencyIcon(currencyShortName))
                    setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
                }
                withdrawAllAmountTextView.setDisablingClickListener {
                    amountLayout.amountEditText.setText(wallet.getTotal().toDisplayStringBy())
                }
                remainsContainer.totalValueTextView.text = Html.fromHtml(
                    withdrawType.tokensRemains,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            }
        }
    }

    override fun showFee(fee: String) {
        binding.withdrawFeeTitle.visible()
        binding.withdrawFee.text = fee
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun navigateWithdrawConfirmation(actionId: Int, bundle: Bundle) {
        navigate(actionId, bundle)
    }

    override fun showAmountError(error: String?) {
        binding.amountLayout.amountInputLayout.error = error
    }

    override fun showReceiveAddressError(error: String?) {
        binding.receiveAddressLayout.receiveAddressInputLayout.error = error
    }

    override fun showMemoError(error: String?) {
        binding.memoLayout.memoInputLayout.error = error
    }

    override fun showTfaError(error: String?) {
        binding.tfaCodeLayout.tfaCodeInputLayout.error = error
    }

    companion object {

        private const val KEY_WITHDRAW_TYPE = "KEY_WITHDRAW_TYPE"

        fun getBundle(withdrawType: WithdrawType): Bundle {
            return Bundle().apply {
                putParcelable(KEY_WITHDRAW_TYPE, withdrawType)
            }
        }
    }
}