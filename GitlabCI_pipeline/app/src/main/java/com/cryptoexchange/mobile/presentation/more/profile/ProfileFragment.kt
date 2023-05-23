package com.cryptoexchange.mobile.presentation.more.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentProfileBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.setDisablingClickListener
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileView {

    private lateinit var binding: FragmentProfileBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            buttonBack.setOnClickListener { presenter.onBackPressed() }
            securitySettingsLayout.changePasswordTextView.setDisablingClickListener {
                presenter.onChangePasswordClicked()
            }
            securitySettingsLayout.twoFATextView.setDisablingClickListener {
                presenter.onTwoFAClicked()
            }
        }
    }

    override fun navigateTo(@IdRes actionId: Int) {
        navigate(actionId)
    }

    override fun navigateBack() {
        popBackStack()
    }
}
