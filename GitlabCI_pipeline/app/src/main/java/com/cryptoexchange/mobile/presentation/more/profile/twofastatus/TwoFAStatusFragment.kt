package com.cryptoexchange.mobile.presentation.more.profile.twofastatus

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTwoFaStatusBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TwoFAStatusFragment : BaseFragment(), TwoFAStatusView {

    private lateinit var binding: FragmentTwoFaStatusBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: TwoFAStatusPresenter

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
        binding = FragmentTwoFaStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonDisable.setOnClickListener {
                presenter.onDisableClicked()
            }
        }
    }

    override fun navigateToTwoFADisabling(actionId: Int) {
        navigate(actionId)
    }

    override fun navigateBack() {
        popBackStack()
    }
}
