package com.cryptoexchange.mobile.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

typealias FragmentViewBindingInflateProvider<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseBindingFragment<VB> : BaseFragment() where VB : ViewBinding {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding().invoke(inflater, container, false)

        return binding.root
    }

    protected abstract fun getBinding(): FragmentViewBindingInflateProvider<VB>
}
