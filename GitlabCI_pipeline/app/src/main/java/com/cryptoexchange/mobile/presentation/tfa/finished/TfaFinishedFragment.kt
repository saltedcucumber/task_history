package com.cryptoexchange.mobile.presentation.tfa.finished

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTfaFinishedBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.openEmailClient
import com.cryptoexchange.mobile.extensions.popBackStack
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TfaFinishedFragment : BaseFragment(), TfaFinishedView {

    private lateinit var binding: FragmentTfaFinishedBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: TfaFinishedPresenter

    @ProvidePresenter
    fun providePresenter(): TfaFinishedPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTfaFinishedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            buttonProceed.setOnClickListener { presenter.onProceedClicked() }
        }
    }

    override fun navigateBack() {
        popBackStack()
    }

    override fun endTfaFlow() {
        openEmailClient()
        popBackStack()
    }
}
