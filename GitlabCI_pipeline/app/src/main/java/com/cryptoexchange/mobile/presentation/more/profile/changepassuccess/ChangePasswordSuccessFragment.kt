package com.cryptoexchange.mobile.presentation.more.profile.changepassuccess

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentChangePasswordSuccessBinding
import com.cryptoexchange.mobile.di.AppInjector
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.restart
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ChangePasswordSuccessFragment : BaseFragment(), ChangePasswordSuccessView {

    private lateinit var binding: FragmentChangePasswordSuccessBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: ChangePasswordSuccessPresenter

    @ProvidePresenter
    fun providePresenter(): ChangePasswordSuccessPresenter = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordSuccessBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        binding.logoutButton.setOnClickListener { presenter.onLogOutClicked() }
        registerOnBackPressedListener { presenter.onLogOutClicked() }
    }

    override fun restart() {
        AppInjector.recreateComponent(requireActivity().applicationContext)
        requireActivity().restart()
    }
}