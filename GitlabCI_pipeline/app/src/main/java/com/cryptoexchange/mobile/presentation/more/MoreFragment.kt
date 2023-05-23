package com.cryptoexchange.mobile.presentation.more

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentMoreBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.restart
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MoreFragment : BaseFragment(), MoreView {

    private lateinit var binding: FragmentMoreBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: MorePresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            logoutButton.setOnClickListener { presenter.onLogoutClicked() }
            activityLogButton.setOnClickListener { presenter.onActivityLogClicked() }
            documentsButton.setOnClickListener { presenter.onDocumentsClicked() }
            faqButton.setOnClickListener { presenter.onFaqClicked() }
            profileSettingsButton.setOnClickListener { presenter.onProfileSettingsClicked() }
            referralsButton.setOnClickListener { presenter.onReferralsClicked() }
            tradingHistoryButton.setOnClickListener { presenter.onTradingHistoryClicked() }
            transactionHistoryButton.setOnClickListener { presenter.onTransactionHistoryClicked() }
            verificationButton.setOnClickListener { presenter.onVerificationClicked() }
        }
    }

    override fun restart() {
        requireActivity().restart()
    }

    override fun inDevelopment() {
        Toast.makeText(requireContext(), "In development", Toast.LENGTH_SHORT).show()
    }

    override fun showUserVerificationStatus(
        @DrawableRes statusIconId: Int,
        @StringRes messageId: Int
    ) {
        binding.userVerificationStatus.setImageResource(statusIconId)
        binding.verificationDescription.setText(messageId)
    }

    override fun navigateTo(destinationId: Int) {
        navigate(destinationId)
    }
}
