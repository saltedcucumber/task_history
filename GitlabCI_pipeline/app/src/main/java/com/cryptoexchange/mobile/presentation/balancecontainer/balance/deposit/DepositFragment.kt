package com.cryptoexchange.mobile.presentation.balancecontainer.balance.deposit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentDepositBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetParcelable
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.BalanceTransactionType
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.transactiontype.DepositType
import com.cryptoexchange.source.extensions.getCurrencyIcon
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class DepositFragment : BaseFragment(), DepositView {

    private lateinit var binding: FragmentDepositBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: DepositPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        // FIXME Put to presenters constructor
        val depositType = tryToGetParcelable<DepositType>(DEPOSIT_TYPE_KEY)
        presenter.data(depositType)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
        }
    }

    override fun showReceiveAddress() {
        binding.receiveAddressLayout.receiveAddressInputLayout.isVisible = true
    }

    override fun showReceiveMemo() {
        binding.receiveMemoLayout.receiveMemoInputLayout.isVisible = true
    }

    override fun updateReceiveAddress(receiveAddress: String) {
        with(binding.receiveAddressLayout) {
            receiveAddressEditText.setText(receiveAddress)
            receiveAddressInputLayout.setEndIconOnClickListener {
                presenter.onClickCopyToClipboard(receiveAddress)
            }
        }
    }

    override fun updateReceiveMemo(receiveMemo: String) {
        with(binding.receiveMemoLayout) {
            receiveMemoEditText.setText(receiveMemo)
            receiveMemoInputLayout.setEndIconOnClickListener {
                presenter.onClickCopyToClipboard(receiveMemo)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateUIWithArguments(balanceTransactionType: BalanceTransactionType) {
        with(binding) {
            val currencyShortName = balanceTransactionType.wallet.currencyShortName
            tokenTextView.apply {
                text = currencyShortName
                // TODO update by backend value
                val icon = ContextCompat.getDrawable(context, getCurrencyIcon(currencyShortName))
                setCompoundDrawablesWithIntrinsicBounds(
                    icon, null, null, null
                )
            }
            remainsContainer.totalValueTextView.text = Html.fromHtml(
                balanceTransactionType.tokensRemains,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {

        private const val DEPOSIT_TYPE_KEY = "DEPOSIT_TYPE_KEY"

        fun getBundle(depositType: DepositType): Bundle {
            return Bundle().apply {
                putParcelable(DEPOSIT_TYPE_KEY, depositType)
            }
        }
    }
}