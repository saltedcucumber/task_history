package com.cryptoexchange.mobile.presentation.auth.resetpasssuccess

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentResetPassSuccessBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.openEmailClient
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetString
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ResetPassSuccessFragment : BaseFragment(), ResetPassSuccessView {

    private lateinit var binding: FragmentResetPassSuccessBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: ResetPassSuccessPresenter

    @ProvidePresenter
    fun providePresenter(): ResetPassSuccessPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)

        val email = tryToGetString(KEY_USER_EMAIL)
        presenter.data(email)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPassSuccessBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonProceed.setOnClickListener { presenter.onProceedClicked() }
            buttonBack.setOnClickListener { presenter.onBackPressed() }
        }
        registerOnBackPressedListener { presenter.onBackPressed() }
    }

    override fun showSuccessMessage(email: String) {
        binding.resetPasswordEmail.text = email
    }

    override fun openEmail() {
        openEmailClient()
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {
        private const val KEY_USER_EMAIL = "KEY_USER_EMAIL"

        fun getBundle(email: String): Bundle =
            Bundle().apply {
                putString(KEY_USER_EMAIL, email)
            }
    }
}