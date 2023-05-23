package com.cryptoexchange.mobile.presentation.auth.logintfa

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTfaLoginBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetBoolean
import com.cryptoexchange.mobile.extensions.value
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TfaLoginFragment : BaseFragment(), TfaLoginView {

    private lateinit var binding: FragmentTfaLoginBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: TfaLoginPresenter

    @ProvidePresenter
    fun providePresenter(): TfaLoginPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)

        val isCreatePasswordFlow = tryToGetBoolean(KEY_IS_CREATE_PASSWORD_FLOW)
        presenter.setData(isCreatePasswordFlow)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTfaLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            tfaLoginButton.setOnClickListener { presenter.onSignInClicked(tfaCodeInput.value) }
        }
        setupTitleGradient()

        registerOnBackPressedListener { presenter.onBackPressed() }
    }

    override fun navigateTo(destinationId: Int) {
        navigate(destinationId)
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun showMissingTwoFaError(message: String) {
        binding.tfaCodeLayout.error = message
    }

    private fun setupTitleGradient() {
        with(binding.tfaLoginTitleTwo) {
            val colors = intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.malibu),
                ContextCompat.getColor(requireContext(), R.color.melrose)
            )
            val width = paint.measureText(text.toString())
            val textShader: Shader = LinearGradient(
                0f,
                0f,
                width,
                textSize,
                colors,
                null,
                Shader.TileMode.CLAMP
            )
            paint.shader = textShader
        }
    }

    companion object {
        private const val KEY_IS_CREATE_PASSWORD_FLOW = "KEY_IS_CREATE_PASSWORD_FLOW"

        fun getBundle(isCreatePasswordFlow: Boolean) =
            Bundle().apply {
                putBoolean(KEY_IS_CREATE_PASSWORD_FLOW, isCreatePasswordFlow)
            }
    }
}