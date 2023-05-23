package com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.cancellation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentWithdrawCancellationBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class WithdrawCancellationFragment : BaseFragment(), WithdrawCancellationView {

    private lateinit var binding: FragmentWithdrawCancellationBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: WithdrawCancellationPresenter

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
        binding = FragmentWithdrawCancellationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun showTfaError(error: String?) {
        binding.tfaCodeInputLayout.error = error
    }

    override fun backToBalance(destinationId: Int) {
        popBackStack(R.id.moreInfoDestination, true)
        navigate(destinationId)
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun setupUI() {
        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }

            proceedButton.setDisablingClickListener {
                presenter.onProceedClicked(tfaCodeEditText.value)
            }
        }
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
