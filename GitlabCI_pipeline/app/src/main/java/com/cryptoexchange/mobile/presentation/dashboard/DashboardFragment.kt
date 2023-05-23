package com.cryptoexchange.mobile.presentation.dashboard

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentDashboardBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.domain.entity.CurrencyHistory
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.visibleUnless
import com.cryptoexchange.mobile.presentation.dashboard.adapter.CurrenciesAdapter
import com.cryptoexchange.mobile.presentation.main.OnTabChangeListener
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class DashboardFragment : BaseFragment(), DashboardView {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: CurrenciesAdapter
    private lateinit var tabChangeListener: OnTabChangeListener

    @Inject
    @InjectPresenter
    lateinit var presenter: DashboardPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onAttach(context: Context) {
        FragmentInjector.getComponent().inject(this)
        super.onAttach(context)

        tabChangeListener = requireParentFragment().parentFragment as? OnTabChangeListener
            ?: throw IllegalArgumentException(
                "Parent fragment should implement OnTabChangeListener"
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        with(binding) {
            accountOverviewDropdown.setOnClickListener { presenter.onDropdownClicked() }
            tfaButton.setOnClickListener { presenter.onTfaClicked() }
            exchangeButton.setOnClickListener { presenter.onExchangeClicked() }
            depositButton.setOnClickListener { presenter.onDepositClicked() }
            moreNewsButton.setOnClickListener { presenter.onNewsClicked() }

            adapter = CurrenciesAdapter()
            currencyRecycler.layoutManager = LinearLayoutManager(requireContext())
            currencyRecycler.adapter = adapter
        }
    }

    override fun showAccountOverview() {
        with(binding) {
            val isShow = accountProtectionIcon.isVisible

            if (!isShow) {
                moveViewsToShow()
            } else {
                moveViewsToHide()
            }

            accountProtectionIcon.visibleUnless(isShow)
            tfaButton.visibleUnless(isShow)
            accountOverviewDivider.visibleUnless(isShow)
            emailIcon.visibleUnless(isShow)
            emailTitle.visibleUnless(isShow)
            emailValue.visibleUnless(isShow)

            val dropdownIconId = if (isShow) {
                R.drawable.ic_dropdown_collapsed
            } else {
                R.drawable.ic_dropdown_open
            }

            accountOverviewDropdown.setImageResource(dropdownIconId)
        }
    }

    override fun showTfaStatus(isVerified: Boolean) {
        with(binding) {
            if (isVerified) {
                accountProtectionIcon.setImageResource(R.drawable.ic_protection_finished)
                tfaButton.setText(R.string.disable_2fa)
            } else {
                accountProtectionIcon.setImageResource(R.drawable.ic_protection_not_finished)
                tfaButton.setText(R.string.activate_2fa_for_full_protection)
            }
        }
    }

    override fun showVerificationStatus(isVerified: Boolean) {
        with(binding) {
            if (isVerified) {
                protectionStatus.setText(R.string.verified)
                protectionStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.puertoRico)
                )
                protectionStatus.background =
                    getStatusDrawable(R.color.frostedMint, protectionStatus.background)
            } else {
                protectionStatus.setText(R.string.unverified)
                protectionStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.flamingo)
                )
                protectionStatus.background =
                    getStatusDrawable(R.color.fairPink, protectionStatus.background)
            }
        }
    }

    override fun showEmail(email: String) {
        binding.emailValue.text = email
    }

    override fun showCrrenciesRate(currenciesRate: List<CurrencyHistory>) {
        adapter.setCurrenciesRates(currenciesRate)
    }

    override fun navigateTo(actionid: Int) {
        navigate(actionid)
    }

    override fun changeNavigationTab(tabId: Int) {
        tabChangeListener.onTabChange(tabId)
    }

    private fun moveViewsToShow() {
        val constraintLayout = binding.constraintLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.protectionStatus,
            ConstraintSet.START,
            R.id.basicProtection,
            ConstraintSet.START,
            0
        )
        constraintSet.connect(
            R.id.protectionStatus,
            ConstraintSet.TOP,
            R.id.basicProtection,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.connect(
            R.id.protectionStatus,
            ConstraintSet.BOTTOM,
            R.id.tfaButton,
            ConstraintSet.TOP,
            0
        )
        constraintSet.clear(R.id.protectionStatus, ConstraintSet.END)
        constraintSet.applyTo(constraintLayout)
    }

    private fun moveViewsToHide() {
        val constraintLayout = binding.constraintLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.protectionStatus,
            ConstraintSet.END,
            R.id.constraintLayout,
            ConstraintSet.END,
            requireContext().resources.getDimension(R.dimen.horizontal_margin).toInt()
        )
        constraintSet.connect(
            R.id.protectionStatus,
            ConstraintSet.TOP,
            R.id.basicProtection,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            R.id.protectionStatus,
            ConstraintSet.BOTTOM,
            R.id.basicProtection,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.clear(R.id.protectionStatus, ConstraintSet.START)
        constraintSet.applyTo(constraintLayout)
    }

    private fun getStatusDrawable(@ColorRes colorId: Int, drawable: Drawable): Drawable {
        val color = ContextCompat.getColor(requireContext(), colorId)
        when (drawable) {
            is ShapeDrawable -> {
                drawable.paint.color = color
            }
            is GradientDrawable -> {
                drawable.setColor(color)
            }
            is ColorDrawable -> {
                drawable.color = color
            }
        }

        return drawable
    }
}
