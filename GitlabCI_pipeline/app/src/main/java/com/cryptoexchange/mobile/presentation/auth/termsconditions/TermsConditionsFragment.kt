package com.cryptoexchange.mobile.presentation.auth.termsconditions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentTermsAndConditionsBinding
import com.cryptoexchange.mobile.extensions.popBackStack

class TermsConditionsFragment : BaseFragment() {

    private lateinit var binding: FragmentTermsAndConditionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        binding.buttonBack.setOnClickListener { popBackStack() }
    }
}