package com.cryptoexchange.mobile.presentation.auth.resetpass

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentResetPasswordBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.value
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ResetPasswordFragment : BaseFragment(), ResetPasswordView {

    private lateinit var binding: FragmentResetPasswordBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: ResetPasswordPresenter

    @ProvidePresenter
    fun providePresenter(): ResetPasswordPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonResetPassword.setOnClickListener { presenter.onResetClicked(emailInput.value) }
            buttonBack.setOnClickListener { presenter.onBackPressed() }
        }
    }

    override fun showEmailError(message: String) {
        binding.emailLayout.error = message
    }

    override fun navigateTo(destinationId: Int, bundle: Bundle) {
        navigate(destinationId, bundle)
    }

    override fun navigateBack() {
        popBackStack()
    }
}
