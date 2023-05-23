package com.cryptoexchange.mobile.presentation.tfa.tfapassword

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTfaPasswordBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.value
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TfaPasswordFragment : BaseFragment(), TfaPasswordView {

    private lateinit var binding: FragmentTfaPasswordBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: TfaPasswordPresenter

    @ProvidePresenter
    fun providePresenter(): TfaPasswordPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTfaPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonAboutTfa.setOnClickListener { presenter.onAboutTfaClicked() }
            buttonStart.setOnClickListener { presenter.onStartClicked(tfaPasswordInput.value) }
            buttonBack.setOnClickListener { presenter.onBackClicked() }
        }
    }

    override fun navigateTo(actionId: Int, bundle: Bundle?) {
        navigate(actionId, bundle)
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun showPasswordError(message: String) {
        binding.tfaPasswordLayout.error = message
    }
}