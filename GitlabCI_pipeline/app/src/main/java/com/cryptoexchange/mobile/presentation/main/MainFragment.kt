package com.cryptoexchange.mobile.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.ui.setupWithNavController
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentMainBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.getNavHostFragment
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.setIsVisibleBy
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MainFragment : BaseFragment(), MainView, OnTabChangeListener {

    private lateinit var binding: FragmentMainBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

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
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupBottomNavigation(@IdRes startDestinations: IntArray) {
        val navController = getNavHostFragment(R.id.mainNavHostFragment)?.navController ?: return

        with(binding.bottomNavigation) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                setIsVisibleBy { startDestinations.contains(destination.id) }
            }
            setupWithNavController(navController)
        }
    }

    override fun suggestTfa(actionId: Int) {
        navigate(actionId)
    }

    override fun onTabChange(@IdRes tabId: Int) {
        binding.bottomNavigation.selectedItemId = tabId
    }
}

interface OnTabChangeListener {

    fun onTabChange(tabId: Int)
}
