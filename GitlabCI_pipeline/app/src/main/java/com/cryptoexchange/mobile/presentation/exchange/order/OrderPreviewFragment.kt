package com.cryptoexchange.mobile.presentation.exchange.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.base.BaseFragment
import com.cryptoexchange.mobile.databinding.FragmentOrderPreviewBinding
import com.cryptoexchange.mobile.di.FragmentInjector
import com.cryptoexchange.mobile.extensions.navigate
import com.cryptoexchange.mobile.extensions.popBackStack
import com.cryptoexchange.mobile.extensions.tryToGetParcelable
import com.cryptoexchange.source.extensions.getCurrencyIcon
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class OrderPreviewFragment : BaseFragment(), OrderPreviewView {

    private lateinit var binding: FragmentOrderPreviewBinding

    @Inject
    @InjectPresenter
    lateinit var presenter: OrderPreviewPresenter

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
        binding = FragmentOrderPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUI() {
        with(binding) {
            registerOnBackPressedListener {
                popBackStack()
            }

            buttonBack.setOnClickListener { presenter.onBackPressed() }

            val info = tryToGetParcelable<OrderPreviewInfo>(ORDER_INFO_KEY)

            info.let {
                tokenTextView.apply {
                    // TODO set icon from backend response in future
                    val icon = ContextCompat.getDrawable(
                        context,
                        getCurrencyIcon(it.paymentType)
                    )
                    setCompoundDrawablesWithIntrinsicBounds(
                        icon, null, null, null
                    )
                    text = it.representationAmount
                }
                paymentTypeLayout.paymentTypeValueTextView.text = it.paymentType
                exchangeCourseLayout.exchangeCourseValueTextView.text = it.representationCourse

                exchangeCommissionLayout.exchangeCommissionValueTextView.text = it.representationFee

                totalExchangePriceLayout.totalValueTextView.text = it.representationTotal
            }
            confirmButton.setOnClickListener {
                presenter.onConfirmClicked(info)
            }
        }
    }

    override fun navigateToSuccessfulConversionScreen() {
        navigate(R.id.action_orderPreviewFragment_to_successfulConversionFragment)
    }

    override fun navigateBack() {
        popBackStack()
    }

    companion object {

        private const val ORDER_INFO_KEY = "ORDER_INFO_KEY"

        fun getBundle(info: OrderPreviewInfo): Bundle {
            return Bundle().apply {
                putParcelable(ORDER_INFO_KEY, info)
            }
        }
    }
}
