package com.cryptoexchange.mobile.presentation.auth.login

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentLoginBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.value
import javax.inject.Inject
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class LoginFragment : BaseFragment(), LoginView {

    private lateinit var binding: FragmentLoginBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter(): LoginPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonLogin.setOnClickListener {
                presenter.onLoginClicked(
                    emailInput.value,
                    passwordInput.value
                )
            }
            buttonForgotPassword.setOnClickListener { presenter.onForgotPasswordClicked() }
            buttonSignUp.setOnClickListener { presenter.onSignUpClicked() }
        }
        setupTitleGradient()
    }

    override fun showEmailError(message: String) {
        binding.emailLayout.error = message
    }

    override fun showPasswordError(message: String) {
        binding.passwordLayout.error = message
    }

    override fun navigateTo(destinationId: Int, bundle: Bundle?) {
        navigate(destinationId, bundle)
    }

    private fun setupTitleGradient() {
        with(binding.loginTitleTwo) {
            val colors = intArrayOf(
                androidx.core.content.ContextCompat.getColor(
                    requireContext(),
                    com.cryptoexchange.mobile.R.color.malibu
                ),
                androidx.core.content.ContextCompat.getColor(
                    requireContext(),
                    com.cryptoexchange.mobile.R.color.melrose
                )
            )
            val width = paint.measureText(text.toString())
            val textShader: Shader = LinearGradient(
                0f,
                0f,
                width,
                textSize,
                colors,
                null,
                android.graphics.Shader.TileMode.CLAMP
            )
            paint.shader = textShader
        }
    }
}
