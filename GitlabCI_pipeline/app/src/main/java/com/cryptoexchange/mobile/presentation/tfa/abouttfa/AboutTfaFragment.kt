package com.cryptoexchange.mobile.presentation.tfa.abouttfa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentAboutTfaBinding
import com.cryptoexchange.mobile.extensions.goneUnless
import com.cryptoexchange.mobile.extensions.popBackStack
import com.google.android.material.button.MaterialButton

class AboutTfaFragment : BaseFragment() {

    private lateinit var binding: FragmentAboutTfaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutTfaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonBack.setOnClickListener { popBackStack() }
            whatIsTfaButton.setOnClickListener { handleClick(whatIsTfaButton, whatIsTfaText) }
            howEnableTfaButton.setOnClickListener {
                handleClick(howEnableTfaButton, howEnableTfaText)
            }
            issuesTfaButton.setOnClickListener { handleClick(issuesTfaButton, issuesTfaText) }
            disableTfaButton.setOnClickListener { handleClick(disableTfaButton, disableTfaText) }
        }
    }

    private fun handleClick(button: MaterialButton, textView: TextView) {
        val visibility = textView.visibility
        val icon = if (visibility == GONE) {
            R.drawable.ic_dropdown_open
        } else {
            R.drawable.ic_dropdown_collapsed
        }
        button.icon = ContextCompat.getDrawable(requireContext(), icon)
        textView.goneUnless(visibility == GONE)
    }
}