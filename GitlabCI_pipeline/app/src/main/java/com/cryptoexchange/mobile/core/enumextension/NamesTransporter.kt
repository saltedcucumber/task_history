package com.cryptoexchange.mobile.core.enumextension

/**
 * Created to take all values of Enum when we do not know what Enum should come to options.
 * @see [com.cryptoexchange.mobile.presentation.exchange.ExchangeType.Companion]
 * @see [com.cryptoexchange.mobile.domain.entity.filter.FilterDisplayValues]
 */
interface NamesTransporter {
    val names: Array<String>
}
