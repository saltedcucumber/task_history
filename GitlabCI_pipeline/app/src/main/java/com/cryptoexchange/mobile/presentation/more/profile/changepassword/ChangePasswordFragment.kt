package com.cryptoexchange.mobile.presentation.more.profile.changepassword

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentChangePasswordBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.value
import com.cryptoexchange.mobile.extensions.visible
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ChangePasswordFragment : BaseFragment(), ChangePasswordView {

    private lateinit var binding: FragmentChangePasswordBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: ChangePasswordPresenter

    @ProvidePresenter
    fun providePresenter(): ChangePasswordPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonUpdatePassword.setOnClickListener {
                presenter.onUpdateClicked(
                    newPasswordInput.value,
                    confirmNewPasswordInput.value,
                    oldPasswordInput.value,
                    tfaCodeInput.value
                )
            }
        }
    }

    override fun showTfaInputView() {
        binding.tfaCodeLayout.visible()
        binding.oldPasswordInput.imeOptions = EditorInfo.IME_ACTION_NEXT
    }

    override fun showOldPasswordError(message: String) {
        binding.oldPasswordLayout.error = message
    }

    override fun showNewPasswordError(message: String) {
        binding.newPasswordLayout.error = message
    }

    override fun showConfirmNewPasswordError(message: String) {
        binding.confirmNewPasswordLayout.error = message
    }

    override fun showTfaCodeError(message: String) {
        binding.tfaCodeLayout.error = message
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun navigateToSuccess() {
        navigate(R.id.changePasswordSuccessFragment)
    }
}