package com.cryptoexchange.mobile.presentation.exchange.exceptions

sealed class ExchangeException(
    override val message: String?
) : Exception()

data class MarketIdToFeeIdException(
    override val message: String = "MarketList does not contain ids for this currencies"
) : ExchangeException(message)
