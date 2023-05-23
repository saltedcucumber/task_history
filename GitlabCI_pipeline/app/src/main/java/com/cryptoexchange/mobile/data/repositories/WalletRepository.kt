package com.cryptoexchange.mobile.data.repositories

import com.cryptoexchange.mobile.domain.entity.CurrencyInBasic
import com.cryptoexchange.source.entity.currency.CurrencyResponse
import com.cryptoexchange.source.entity.currency.converter.CurrencyConvertResponse
import com.cryptoexchange.source.entity.wallet.TransactionsModel
import com.cryptoexchange.source.entity.wallet.WalletModel
import com.cryptoexchange.source.entity.wallet.address.DepositAddressResponse
import com.cryptoexchange.source.entity.wallet.transaction.TransactionQueries
import com.cryptoexchange.source.entity.withdraw.PayGateType
import com.cryptoexchange.source.entity.withdraw.request.MoneyTransactionModel
import com.cryptoexchange.source.entrypoint.deeplink.DeepLinkConfirmWithdraw
import com.cryptoexchange.source.entrypoint.managers.CurrencyConverterManager
import com.cryptoexchange.source.entrypoint.managers.WalletManager
import com.cryptoexchange.source.entrypoint.managers.WithdrawalManager
import com.cryptoexchange.source.extensions.getBaseCurrencyToConvert
import io.reactivex.rxjava3.core.Single
import java.math.BigDecimal

class WalletRepository(
    private val walletManager: WalletManager,
    private val currencyConverterManager: CurrencyConverterManager,
    private val withdrawalManager: WithdrawalManager
) {

    fun getWalletList(): Single<List<WalletModel>> =
        Single.fromCallable { walletManager.getWalletList() }

    fun getDepositAddress(walletId: Long): Single<DepositAddressResponse> =
        Single.fromCallable { walletManager.getDepositAddress(walletId) }

    fun getListAccountDepositAddresses(): Single<List<DepositAddressResponse>> =
        Single.fromCallable { walletManager.getListAccountDepositAddresses() }

    fun getPrices(currencyCodes: List<Pair<String, BigDecimal>>): Single<List<CurrencyInBasic>> =
        Single.create { emitter ->
            val portfolio = mutableListOf<CurrencyInBasic>()
            currencyCodes.forEach { (currencyCode, available) ->
                val priceList = currencyConverterManager.getRates(currencyCode)
                val currencyPrice =
                    priceList.find { it.shortName.equals(getBaseCurrencyToConvert(), true) }

                if (currencyPrice != null) {
                    val currencyInUsd = CurrencyInBasic(
                        currencyCode,
                        available.multiply(currencyPrice.price)
                    )
                    portfolio.add(currencyInUsd)
                }
            }
            emitter.onSuccess(portfolio)
        }

    fun getTransactions(queries: TransactionQueries): Single<TransactionsModel> =
        Single.fromCallable { walletManager.getTransactions(queries) }

    fun requestWithdraw(
        amount: BigDecimal,
        currencyId: Long,
        memo: String?,
        payGateType: PayGateType,
        receivingAddress: String,
        twoFaCode: String
    ): Single<MoneyTransactionModel> =
        Single.fromCallable {
            withdrawalManager.requestWithdraw(
                amount,
                currencyId,
                memo,
                payGateType,
                receivingAddress,
                twoFaCode
            )
        }

    fun confirmWithdraw(
        action: DeepLinkConfirmWithdraw
    ): Single<MoneyTransactionModel> =
        Single.fromCallable { withdrawalManager.confirmWithdraw(action) }

    fun resendWithdrawConfirmationEmail(
        walletId: Long
    ): Single<MoneyTransactionModel> =
        Single.fromCallable { withdrawalManager.resendWithdrawConfirmationEmail(walletId) }

    fun cancelWithdraw(id: Long, twoFaCode: String): Single<MoneyTransactionModel> =
        Single.fromCallable { withdrawalManager.cancelWithdraw(id, twoFaCode) }

    fun convertWithRate(
        depositInfo: DepositAddressResponse,
        currencies: List<CurrencyResponse>
    ): Single<List<CurrencyConvertResponse>> =
        Single.create { emitter ->
            assert(currencies.isNotEmpty()) {
                "Currencies list is empty"
            }

            val wallet = depositInfo.wallet
            val currencyTo = getBaseCurrencyToConvert()
            val currencyToId = currencies
                .find { curr -> curr.shortName == currencyTo }
                ?.id ?: throw IllegalArgumentException("CurrencyToId is not found")
            val currencyFromId = currencies
                .find { curr -> curr.shortName == wallet.currencyShortName }
                ?.id ?: throw IllegalArgumentException("CurrencyFromId is not found")

            val source = currencyConverterManager.convertWithRate(
                wallet.isAvailable,
                currencyFromId,
                currencyToId
            ).list
            emitter.onSuccess(source)
        }
}
