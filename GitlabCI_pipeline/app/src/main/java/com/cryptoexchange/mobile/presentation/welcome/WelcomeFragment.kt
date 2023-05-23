package com.cryptoexchange.mobile.presentation.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentWelcomeBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import moxy.presenter.InjectPresenter
import javax.inject.Inject

class WelcomeFragment : BaseFragment(), WelcomeView {

    private lateinit var binding: FragmentWelcomeBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: WelcomePresenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonLogin.setOnClickListener { presenter.onLoginClicked() }
            buttonSignUp.setOnClickListener { presenter.onSignUpClicked() }
        }
    }

    override fun navigateTo(destinationId: Int) {
        navigate(destinationId)
    }
}
