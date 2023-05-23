package com.cryptoexchange.mobile.presentation.tfa.disable

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentDisableTfaBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.value
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class DisableTfaFragment : BaseFragment(), DisableTfaView {

    private lateinit var binding: FragmentDisableTfaBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: DisableTfaPresenter

    @ProvidePresenter
    fun providePresenter(): DisableTfaPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisableTfaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonDisable.setOnClickListener {
                presenter.onDisableClicked(
                    tfaPasswordInput.value,
                    tfaCodeInput.value
                )
            }
        }
    }

    override fun showErrorMessages(passwordMessage: String, codeMessage: String) {
        with(binding) {
            tfaPasswordLayout.error = passwordMessage
            tfaCodeLayout.error = codeMessage
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun navigateToDisableSuccess(actionId: Int) {
        navigate(actionId)
    }
}
