package com.cryptoexchange.mobile.presentation.exchange.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentSuccessfulConversionBinding
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.setDisablingClickListener

class SuccessfulConversionFragment : BaseFragment() {

    private lateinit var binding: FragmentSuccessfulConversionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessfulConversionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            registerOnBackPressedListener {
                handleBackWard()
            }

            doneButton.setDisablingClickListener {
                handleBackWard()
            }
        }
    }

    private fun handleBackWard() {
        popBackStack()
    }
}
