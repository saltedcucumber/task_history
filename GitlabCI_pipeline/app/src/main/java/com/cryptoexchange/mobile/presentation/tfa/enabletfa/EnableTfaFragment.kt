package com.cryptoexchange.mobile.presentation.tfa.enabletfa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentEnableTfaBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class EnableTfaFragment : BaseFragment(), EnableTfaView {

    private lateinit var binding: FragmentEnableTfaBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: EnableTfaPresenter

    @ProvidePresenter
    fun providePresenter(): EnableTfaPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnableTfaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonCancel.setOnClickListener { presenter.onBackPressed() }
            buttonContinue.setOnClickListener { presenter.onContinuePressed() }
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun navigateForward() {
        navigate(R.id.action_enableTfaFragment_to_tfaPasswordFragment)
    }
}
