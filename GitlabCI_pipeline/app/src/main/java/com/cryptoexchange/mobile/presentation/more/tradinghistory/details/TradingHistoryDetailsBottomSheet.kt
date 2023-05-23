package com.cryptoexchange.mobile.presentation.more.tradinghistory.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptoexchange.mobile.databinding.FragmentTradingHistoryDetailsBinding
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetParcelable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TradingHistoryDetailsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTradingHistoryDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTradingHistoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(tryToGetParcelable(TRADING_DETAILS_KEY))
    }

    private fun setupUI(details: TradingHistoryDetails) {
        with(binding) {
            crossButton.setOnClickListener {
                popBackStack()
            }
            with(details) {
                orderIdValueTextView.text = orderId.toString()
                typeValueTextView.text = getDisplayType(requireContext())
                currencyValueTextView.text = leftCurrency
                dateValueTextView.text = date
                sideValueTextView.text = getDisplaySide(requireContext())
                priceValueTextView.text = displayPrice
                feeValueTextView.text = displayFee
                totalValueTextView.text = displayTotal
            }
        }
    }

    companion object {

        private const val TRADING_DETAILS_KEY = "TRADING_DETAILS_KEY"

        fun getBundle(
            details: TradingHistoryDetails
        ): Bundle {
            return Bundle().apply {
                putParcelable(TRADING_DETAILS_KEY, details)
            }
        }
    }
}