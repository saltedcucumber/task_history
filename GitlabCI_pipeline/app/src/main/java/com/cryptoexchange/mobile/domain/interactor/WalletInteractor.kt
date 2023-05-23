package com.cryptoexchange.mobile.domain.interactor

import com.cryptoexchange.mobile.core.global.schedulers.SchedulersProvider
import com.cryptoexchange.mobile.data.repositories.WalletRepository
import com.cryptoexchange.mobile.domain.entity.CurrencyInBasic
import com.cryptoexchange.mobile.domain.entity.backendwrappers.CurrencyWrapper
import com.cryptoexchange.source.entity.UnauthorizedException
import com.cryptoexchange.source.entity.currency.converter.CurrencyConvertResponse
import com.cryptoexchange.source.entity.error.FailedRequestException
import com.cryptoexchange.source.entity.error.UnhandledErrResponseException
import com.cryptoexchange.source.entity.wallet.TransactionsModel
import com.cryptoexchange.source.entity.wallet.WalletModel
import com.cryptoexchange.source.entity.wallet.address.DepositAddressResponse
import com.cryptoexchange.source.entity.wallet.transaction.TransactionQueries
import com.cryptoexchange.source.entity.withdraw.PayGateType
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkConfirmWithdraw
import com.cryptoexchange.source.extensions.getBaseCurrencyToConvert
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.internal.operators.single.SingleDoOnSuccess
import java.math.BigDecimal
import javax.inject.Inject

class WalletInteractor @Inject constructor(
    private val walletRepository: WalletRepository,
    private val schedulers: SchedulersProvider,
    private val exchangeInteractor: ExchangeInteractor
) {

    fun getWalletList(): Single<List<WalletModel>> =
        walletRepository
            .getWalletList()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getTransactionInfo(walletId: Long): Single<Pair<DepositAddressResponse, CurrencyWrapper>> =
        handleDepositAddressResponse(walletId)
            .flatMap({
                walletRepository.convertWithRate(it, exchangeInteractor.cachedCurrencies)
            }, { depositInfo: DepositAddressResponse, rate: List<CurrencyConvertResponse> ->
                val currencyWrapper = rate.first().run {
                    CurrencyWrapper(
                        fromCurrencyId,
                        fromCurrencyCode,
                        Triple(amount, converted, toCurrencyCode)
                    )
                }
                Pair(depositInfo, currencyWrapper)
            })
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun handleDepositAddressResponse(walletId: Long): Single<DepositAddressResponse> =
        walletRepository
            .getListAccountDepositAddresses()
            .onErrorResumeNext {
                if (it is FailedRequestException ||
                    it is UnauthorizedException ||
                    it is UnhandledErrResponseException
                ) {
                    Single.error(it)
                } else {
                    SingleDoOnSuccess.create {
                        emptyList<DepositAddressResponse>()
                    }
                }
            }
            .map {
                val index = it.indexOfFirst { address -> address.wallet.id == walletId }
                if (index == -1) {
                    walletRepository.getDepositAddress(walletId).blockingGet()
                } else {
                    it[index]
                }
            }

    fun getCurrenciesPortfolio(): Single<List<CurrencyInBasic>> =
        walletRepository
            .getWalletList()
            .map { walletModels ->
                val nonEmpty = walletModels.filter {
                    !it.currencyShortName.equals(getBaseCurrencyToConvert(), true) &&
                        it.getTotal().compareTo(BigDecimal.ZERO) > 0
                }
                nonEmpty.map { Pair(it.currencyShortName, it.isAvailable) }
            }
            .flatMap { walletRepository.getPrices(it) }
            .onErrorResumeNext {
                val error = if (it is CompositeException) {
                    it.exceptions[0]
                } else {
                    it
                }

                Single.error(error)
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getTransactions(queries: TransactionQueries): Single<TransactionsModel> =
        walletRepository
            .getTransactions(queries)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun requestWithdraw(
        amount: BigDecimal,
        currencyId: Long,
        memo: String?,
        payGateType: PayGateType,
        receivingAddress: String,
        twoFaCode: String
    ): Single<MoneyTransactionModel> = walletRepository
        .requestWithdraw(amount, currencyId, memo, payGateType, receivingAddress, twoFaCode)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun confirmWithdraw(
        action: DeepLinkConfirmWithdraw
    ): Single<MoneyTransactionModel> = walletRepository
        .confirmWithdraw(action)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun resendWithdrawConfirmationEmail(
        walletId: Long
    ): Single<MoneyTransactionModel> = walletRepository
        .resendWithdrawConfirmationEmail(walletId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun cancelWithdraw(
        id: Long,
        twoFaCode: String
    ): Single<MoneyTransactionModel> = walletRepository
        .cancelWithdraw(id, twoFaCode)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getAmountRestrictions(currencyId: Long): Pair<Boolean, BigDecimal?>? =
        exchangeInteractor.cachedCurrencies
            .find { it.id == currencyId }?.run {
                hasMinimumWithdrawAmount to minWithdrawAmount
            }
}
