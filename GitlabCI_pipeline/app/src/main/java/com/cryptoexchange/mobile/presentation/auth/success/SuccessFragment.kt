package com.cryptoexchange.mobile.presentation.auth.success

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentSuccessBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.getNavHostFragment
import com.cryptoexchange.mobile.extensions.openEmailClient
import com.cryptoexchange.mobile.extensions.popBackStack
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SuccessFragment : BaseFragment(), SuccessView {

    private lateinit var binding: FragmentSuccessBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: SuccessPresenter

    @ProvidePresenter
    fun providePresenter(): SuccessPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonContinue.setOnClickListener { presenter.onContinueClicked() }
            buttonResendEmail.setOnClickListener { presenter.onResendClicked() }
        }
    }

    override fun openEmail() {
        openEmailClient()
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun registerOnBackPressedListener() {
        val host = getNavHostFragment(R.id.appNavHostFragment) ?: return
        registerOnBackPressedListener {
            if (!host.navController.popBackStack()) {
                presenter.onBackPressed()
            }
        }
    }
}