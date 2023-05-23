package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.confirmation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentWithdrawConfirmationBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class WithdrawConfirmationFragment : BaseFragment(), WithdrawConfirmationView {

    private lateinit var binding: FragmentWithdrawConfirmationBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: WithdrawConfirmationPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        val withdrawId = tryToGetLong(WITHDRAW_ID_KEY)
        presenter.setData(withdrawId)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }

            goToMailboxButton.setDisablingClickListener {
                presenter.onMailboxClicked()
            }

            resendEmailButton.setDisablingClickListener {
                presenter.onResendClicked()
            }

            cancelTransactionTextView.setDisablingClickListener {
                presenter.onCancelClicked()
            }
        }
    }

    override fun openEmail() {
        openEmailClient()
    }

    override fun navigateToWithdrawCancellation(actionId: Int, bundle: Bundle) {
        navigate(actionId, bundle)
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {

        private const val WITHDRAW_ID_KEY = "WITHDRAW_ID_KEY"

        fun getBundle(
            withdrawId: Long
        ): Bundle {
            return Bundle().apply {
                putLong(WITHDRAW_ID_KEY, withdrawId)
            }
        }
    }
}
