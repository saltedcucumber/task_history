package com.cryptoexchange.mobile.presentation.auth.createpass

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkCreateNewPass
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentCreatePasswordBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class CreatePasswordFragment : BaseFragment(), CreatePasswordView {

    private lateinit var binding: FragmentCreatePasswordBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: CreatePasswordPresenter

    @ProvidePresenter
    fun providePresenter(): CreatePasswordPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)

        val deepLinkData = tryToGetParcelable<DeepLinkCreateNewPass>(KEY_DEEP_LINK_DATA)
        presenter.data(deepLinkData)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonCreatePassword.setOnClickListener {
                presenter.onCreatePasswordClicked(passwordInput.value, confirmPasswordInput.value)
            }
            buttonBack.setOnClickListener { presenter.onBackPressed() }
        }
    }

    override fun showConfirmPasswordError(message: String) {
        binding.confirmPasswordLayout.error = message
    }

    override fun showPasswordError(message: String) {
        binding.passwordLayout.error = message
    }

    override fun navigateTo(actionId: Int, bundle: Bundle?) {
        navigate(actionId, bundle)
    }

    override fun navigateBack() {
        navigate(R.id.action_createPasswordFragment_to_welcomeFragment)
    }

    override fun registerOnBackPressedListener() {
        val host = getNavHostFragment(R.id.appNavHostFragment) ?: return
        registerOnBackPressedListener {
            if (!host.navController.popBackStack()) {
                presenter.onBackPressed()
            }
        }
    }

    override fun restart() {
        requireActivity().restart()
    }

    companion object {
        private const val KEY_DEEP_LINK_DATA = "KEY_DEEP_LINK_DATA"

        fun getBundle(deepLinkAction: DeepLinkCreateNewPass): Bundle =
            Bundle().apply {
                putParcelable(KEY_DEEP_LINK_DATA, deepLinkAction)
            }
    }
}