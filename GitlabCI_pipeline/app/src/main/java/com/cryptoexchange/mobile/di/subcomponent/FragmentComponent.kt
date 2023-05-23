package com.cryptoexchange.mobile.di.subcomponent

import com.cryptoexchange.mobile.presentation.dashboard.DashboardFragment
import com.cryptoexchange.mobile.presentation.auth.createpass.CreatePasswordFragment
import com.cryptoexchange.mobile.presentation.auth.login.LoginFragment
import com.cryptoexchange.mobile.presentation.auth.signup.SignUpFragment
import com.cryptoexchange.mobile.presentation.auth.resetpass.ResetPasswordFragment
import com.cryptoexchange.mobile.presentation.auth.resetpasssuccess.ResetPassSuccessFragment
import com.cryptoexchange.mobile.presentation.welcome.WelcomeFragment
import com.cryptoexchange.mobile.presentation.tfa.enabletfa.EnableTfaFragment
import com.cryptoexchange.mobile.presentation.main.MainFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.BalanceContainerFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.BalanceFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.deposit.DepositFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.WithdrawFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.portfolio.PortfolioFragment
import com.cryptoexchange.mobile.presentation.exchange.ExchangeFragment
import com.cryptoexchange.mobile.presentation.exchange.filters.CurrencyChooserFragment
import com.cryptoexchange.mobile.presentation.exchange.order.OrderPreviewFragment
import com.cryptoexchange.mobile.presentation.auth.logintfa.TfaLoginFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.cancellation.WithdrawCancellationFragment
import com.cryptoexchange.mobile.presentation.balancecontainer.balance.withdraw.confirmation.WithdrawConfirmationFragment
import com.cryptoexchange.mobile.presentation.auth.success.SuccessFragment
import com.cryptoexchange.mobile.presentation.dashboard.news.NewsFragment
import com.cryptoexchange.mobile.presentation.more.MoreFragment
import com.cryptoexchange.mobile.presentation.more.activitylog.ActivityLogFragment
import com.cryptoexchange.mobile.presentation.more.tradinghistory.TradingHistoryFragment
import com.cryptoexchange.mobile.presentation.more.transactionhistory.TransactionHistoryFragment
import com.cryptoexchange.mobile.presentation.more.profile.ProfileFragment
import com.cryptoexchange.mobile.presentation.more.profile.changepassuccess.ChangePasswordSuccessFragment
import com.cryptoexchange.mobile.presentation.more.profile.changepassword.ChangePasswordFragment
import com.cryptoexchange.mobile.presentation.more.profile.twofastatus.TwoFAStatusFragment
import com.cryptoexchange.mobile.presentation.trading.TradingFragment
import com.cryptoexchange.mobile.presentation.tfa.backupkey.TfaBackupKeyFragment
import com.cryptoexchange.mobile.presentation.tfa.code.TfaCodeFragment
import com.cryptoexchange.mobile.presentation.tfa.disable.DisableTfaFragment
import com.cryptoexchange.mobile.presentation.tfa.disablesuccess.DisableTfaSuccessFragment
import com.cryptoexchange.mobile.presentation.tfa.finished.TfaFinishedFragment
import com.cryptoexchange.mobile.presentation.tfa.tfapassword.TfaPasswordFragment
import com.cryptoexchange.mobile.presentation.more.tradinghistory.filter.TradingHistoryFilterFragment
import com.cryptoexchange.mobile.presentation.more.transactionhistory.filter.TransactionHistoryFilterFragment
import com.cryptoexchange.mobile.presentation.trading.market.CurrencyMarketFragment
import com.cryptoexchange.mobile.presentation.trading.chart.ChartFragment
import com.cryptoexchange.mobile.presentation.trading.myorders.MyOrdersFragment
import com.cryptoexchange.mobile.presentation.trading.orders.OrdersFragment
import com.cryptoexchange.mobile.presentation.trading.ordershistory.OrdersHistoryFragment
import dagger.Subcomponent

@Subcomponent
interface FragmentComponent {

    fun inject(fragment: MainFragment)

    fun inject(fragment: DashboardFragment)
    fun inject(fragment: ExchangeFragment)
    fun inject(fragment: OrderPreviewFragment)
    fun inject(fragment: CurrencyChooserFragment)

    fun inject(fragment: TradingFragment)
    fun inject(fragment: CurrencyMarketFragment)
    fun inject(fragment: ChartFragment)
    fun inject(fragment: MyOrdersFragment)
    fun inject(fragment: OrdersHistoryFragment)
    fun inject(fragment: OrdersFragment)

    fun inject(fragment: MoreFragment)
    fun inject(fragment: TradingHistoryFragment)
    fun inject(fragment: TradingHistoryFilterFragment)
    fun inject(fragment: TransactionHistoryFragment)
    fun inject(fragment: TransactionHistoryFilterFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: TwoFAStatusFragment)

    fun inject(fragment: WelcomeFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: TfaLoginFragment)
    fun inject(fragment: SignUpFragment)
    fun inject(fragment: SuccessFragment)
    fun inject(fragment: ResetPasswordFragment)
    fun inject(fragment: CreatePasswordFragment)
    fun inject(fragment: ResetPassSuccessFragment)
    fun inject(fragment: EnableTfaFragment)
    fun inject(fragment: TfaPasswordFragment)
    fun inject(fragment: TfaBackupKeyFragment)
    fun inject(fragment: TfaCodeFragment)
    fun inject(fragment: TfaFinishedFragment)

    fun inject(fragment: BalanceContainerFragment)
    fun inject(fragment: PortfolioFragment)

    fun inject(fragment: BalanceFragment)
    fun inject(fragment: DepositFragment)
    fun inject(fragment: WithdrawFragment)
    fun inject(fragment: WithdrawConfirmationFragment)
    fun inject(fragment: WithdrawCancellationFragment)

    fun inject(fragment: DisableTfaFragment)
    fun inject(fragment: DisableTfaSuccessFragment)
    fun inject(fragment: ChangePasswordFragment)
    fun inject(fragment: ChangePasswordSuccessFragment)
    fun inject(fragment: ActivityLogFragment)
    fun inject(fragment: NewsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }
}
