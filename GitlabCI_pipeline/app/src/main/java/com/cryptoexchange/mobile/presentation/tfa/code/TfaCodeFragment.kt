package com.cryptoexchange.mobile.presentation.tfa.code

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTfaCodeBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetString
import com.cryptoexchange.mobile.extensions.value
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TfaCodeFragment : BaseFragment(), TfaCodeView {

    private lateinit var binding: FragmentTfaCodeBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: TfaCodePresenter

    @ProvidePresenter
    fun providePresenter(): TfaCodePresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        val manualKey = tryToGetString(KEY_MANUAL_KEY)
        presenter.data(manualKey)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTfaCodeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonEnableTfa.setOnClickListener { presenter.onTfaEnableClicked(tfaCodeInput.value) }
            buttonPlayMarket.setOnClickListener { presenter.onOpenPlayMarketClicked() }
            tfaManualKeyLayout.setEndIconOnClickListener { presenter.onCopyClicked() }
        }
    }

    override fun showManualKey(key: String) {
        binding.tfaManualKeyInput.setText(key)
    }

    override fun openAuthenticatorOnMarket() {
        try {
            requireActivity()
                .startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DEEP_LINK_AUTHENTICATOR)))
        } catch (anfe: ActivityNotFoundException) {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(APP_LINK_AUTHENTICATOR)
                )
            )
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun navigateToFinish(actionId: Int) {
        navigate(actionId)
    }

    override fun showCodeError(message: String) {
        binding.tfaCodeLayout.error = message
    }

    override fun showCopyResult(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val DEEP_LINK_AUTHENTICATOR =
            "market://details?id=com.google.android.apps.authenticator2"
        private const val APP_LINK_AUTHENTICATOR =
            "https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2"
        private const val KEY_MANUAL_KEY = "KEY_MANUAL_KEY"

        fun getBundle(key: String): Bundle =
            Bundle().apply {
                putString(KEY_MANUAL_KEY, key)
            }
    }
}