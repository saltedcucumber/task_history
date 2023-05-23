package com.cryptoexchange.mobile.presentation.more.transactionhistory.filter

import com.cryptoexchange.source.entity.withdraw.Type
import com.cryptoexchange.mobile.core.enumextension.NamesTransporter
import com.cryptoexchange.mobile.presentation.exchange.ConvertibleToBackend

enum class PaymentType : ConvertibleToBackend<Type> {
    DEPOSIT {
        override val convertedBackendValue: Type
            get() = Type.DEPOSIT
    },
    WITHDRAW {
        override val convertedBackendValue: Type
            get() = Type.WITHDRAW
    },
    REFERRAL {
        override val convertedBackendValue: Type
            get() = Type.REFERRAL
    },
    DIVIDEND {
        override val convertedBackendValue: Type
            get() = Type.DIVIDEND
    };

    companion object : NamesTransporter {

        override val names: Array<String>
            get() = ArrayList<String>(values().size).also {
                values().forEach { paymentType ->
                    it.add(paymentType.name)
                }
            }.toTypedArray()
    }
}
