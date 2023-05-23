package com.cryptoexchange.mobile.di.module

import android.content.Context
import com.cryptoexchange.mobile.BuildConfig
import com.cryptoexchange.source.di.DependencyManager
import com.cryptoexchange.source.entrypoint.SdkEntryPoint
import com.cryptoexchange.mobile.data.repositories.MarketRepository
import com.cryptoexchange.mobile.data.repositories.TradeRepository
import com.cryptoexchange.mobile.data.repositories.UserRepository
import com.cryptoexchange.mobile.data.repositories.WalletRepository
import com.cryptoexchange.mobile.data.repositories.exchange.ExchangeRepository
import com.cryptoexchange.mobile.data.storage.Prefs
import com.cryptoexchange.mobile.di.qualifiers.RestUrl
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractor
import com.cryptoexchange.mobile.domain.interactor.market.TradingMarketInteractorImpl
import com.cryptoexchange.source.entrypoint.managers.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAppPrefs(context: Context): Prefs = Prefs(context)

    @Provides
    @RestUrl
    fun provideBaseUrl(): String = BuildConfig.DEV_UNITEX

    @Provides
    @Singleton
    fun provideSDKDependencyManager(): DependencyManager = SdkEntryPoint.dependencyManager

    @Provides
    @Singleton
    fun provideAuthManager(dependencyManager: DependencyManager): UserManager =
        dependencyManager.userManager

    @Provides
    @Singleton
    fun provideWalletManager(dependencyManager: DependencyManager): WalletManager =
        dependencyManager.walletManager

    @Provides
    @Singleton
    fun provideWalletRepository(
        walletManager: WalletManager,
        currencyConverterManager: CurrencyConverterManager,
        withdrawalManager: WithdrawalManager
    ): WalletRepository =
        WalletRepository(walletManager, currencyConverterManager, withdrawalManager)

    @Provides
    @Singleton
    fun provideWithdrawManager(dependencyManager: DependencyManager): WithdrawalManager =
        dependencyManager.withdrawalManager

    @Provides
    @Singleton
    fun provideExchangeRepository(
        marketManager: MarketManager,
        orderManager: OrderManager,
        currencyConverterManager: CurrencyConverterManager,
        currencyManager: CurrencyManager
    ): ExchangeRepository =
        ExchangeRepository(marketManager, orderManager, currencyConverterManager, currencyManager)

    @Provides
    @Singleton
    fun provideTradeManager(dependencyManager: DependencyManager): TradeManager =
        dependencyManager.tradeManager

    @Provides
    @Singleton
    fun provideAccountManager(dependencyManager: DependencyManager): AccountManager =
        dependencyManager.accountManager

    @Provides
    @Singleton
    fun provideTradeRepository(
        tradeManager: TradeManager,
        accountManager: AccountManager
    ): TradeRepository =
        TradeRepository(tradeManager, accountManager)

    @Provides
    @Singleton
    fun provideMarketManager(dependencyManager: DependencyManager): MarketManager =
        dependencyManager.marketManager

    @Provides
    @Singleton
    fun provideMarketRepository(marketManager: MarketManager): MarketRepository =
        MarketRepository(marketManager)

    @Provides
    @Singleton
    fun provideCurrencyManager(
        dependencyManager: DependencyManager
    ): CurrencyManager = dependencyManager.currencyManager

    @Provides
    @Singleton
    fun provideCurrencyConverterManager(
        dependencyManager: DependencyManager
    ): CurrencyConverterManager = dependencyManager.currencyConverterManager

    @Provides
    @Singleton
    fun provideTradingMarketInteractor(): TradingMarketInteractor = TradingMarketInteractorImpl()

    @Provides
    @Singleton
    fun provideOrderManager(dependencyManager: DependencyManager): OrderManager =
        dependencyManager.orderManager

    @Provides
    @Singleton
    fun provideUserRepository(dependencyManager: DependencyManager, prefs: Prefs): UserRepository =
        UserRepository(dependencyManager.userManager, prefs)
}
