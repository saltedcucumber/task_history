package com.cryptoexchange.mobile.presentation.more.verification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentVerificationBinding
import com.cryptoexchange.mobile.extensions.popBackStack

class VerificationFragment : BaseFragment() {

    private lateinit var binding: FragmentVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun setupUI() {
        super.setupUI()

        with(binding) {
            buttonBack.setOnClickListener { popBackStack() }
            buttonGoToWeb.setOnClickListener { navigateToWeb() }
        }
    }

    private fun navigateToWeb() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(USER_VERIFICATION_URL)

        startActivity(intent)
    }

    companion object {
        private const val USER_VERIFICATION_URL = "http://beta.exchange.unitex.one/settings"
    }
}