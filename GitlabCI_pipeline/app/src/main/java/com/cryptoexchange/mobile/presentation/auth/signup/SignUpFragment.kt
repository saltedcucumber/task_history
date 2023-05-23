package com.cryptoexchange.mobile.presentation.auth.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentSignUpBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.value
import javax.inject.Inject
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class SignUpFragment : BaseFragment(), SignUpView {

    private lateinit var binding: FragmentSignUpBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    @ProvidePresenter
    fun providePresenter(): SignUpPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonSignUp.setOnClickListener {
                presenter.onSignUpClicked(
                    referralCodeInput.value,
                    firstNameInput.value,
                    lastNameInput.value,
                    emailInput.value,
                    passwordInput.value,
                    confirmPasswordInput.value,
                    termsAndConditionsBox.isChecked
                )
            }
            termsAndConditionsButton.setOnClickListener { presenter.onTermsAndConditionsClicked() }
            buttonBack.setOnClickListener { presenter.onBackClicked() }
        }
    }

    override fun showFirstNameError(message: String) {
        binding.firstNameLayout.error = message
    }

    override fun showLastNameError(message: String) {
        binding.lastNameLayout.error = message
    }

    override fun showEmailError(message: String) {
        binding.emailLayout.error = message
    }

    override fun showPasswordError(message: String) {
        binding.passwordLayout.error = message
    }

    override fun showConfirmPasswordError(message: String) {
        binding.confirmPasswordLayout.error = message
    }

    override fun navigateTo(destinationId: Int) {
        navigate(destinationId)
    }

    override fun navigateBack() {
        popBackStack()
    }
}
