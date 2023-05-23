package com.cryptoexchange.mobile.extensions

import com.cryptoexchange.source.entity.withdraw.Type
import com.cryptoexchange.source.entity.withdraw.request.WithdrawStatus
import com.cryptoexchange.mobile.R

fun getOperationType(type: Type): Int =
    when (type) {
        Type.DEPOSIT -> R.string.deposit
        Type.NONE -> R.string.none
        Type.WITHDRAW -> R.string.withdraw
        Type.REFERRAL -> R.string.referral
        Type.DIVIDEND -> R.string.dividend
        Type.UNRECOGNIZED -> R.string.unrecognized
    }

fun getOperationStatus(status: WithdrawStatus): Int =
    when (status) {
        WithdrawStatus.PENDING -> R.string.pending
        WithdrawStatus.CONFIRMED -> R.string.confirmed
        WithdrawStatus.REJECTED -> R.string.rejected
        WithdrawStatus.PAID_OUT -> R.string.paid_out
        WithdrawStatus.CANCELED_BY_USER -> R.string.canceled
        WithdrawStatus.WAITING_FOR_EMAIL_CONFIRM -> R.string.email_confirmation
        WithdrawStatus.SUSPENDED -> R.string.suspended
        WithdrawStatus.WAITING_FOR_BLOCKCHAIN_CONFIRM -> R.string.blockchain_confirmation
        WithdrawStatus.UNDEFINED,
        WithdrawStatus.UNRECOGNIZED -> R.string.unknown_error
    }

fun getStatusTextColor(status: WithdrawStatus): Int =
    when (status) {
        WithdrawStatus.PENDING,
        WithdrawStatus.CONFIRMED,
        WithdrawStatus.WAITING_FOR_EMAIL_CONFIRM,
        WithdrawStatus.WAITING_FOR_BLOCKCHAIN_CONFIRM,
        WithdrawStatus.SUSPENDED -> R.color.selective_yellow
        WithdrawStatus.CANCELED_BY_USER,
        WithdrawStatus.REJECTED,
        WithdrawStatus.UNDEFINED,
        WithdrawStatus.UNRECOGNIZED -> R.color.bittersweet
        WithdrawStatus.PAID_OUT -> R.color.puertoRico
    }

fun getStatusBackgroundColor(status: WithdrawStatus): Int =
    when (status) {
        WithdrawStatus.PENDING,
        WithdrawStatus.CONFIRMED,
        WithdrawStatus.WAITING_FOR_EMAIL_CONFIRM,
        WithdrawStatus.WAITING_FOR_BLOCKCHAIN_CONFIRM,
        WithdrawStatus.SUSPENDED -> R.color.serenade
        WithdrawStatus.CANCELED_BY_USER,
        WithdrawStatus.REJECTED,
        WithdrawStatus.UNDEFINED,
        WithdrawStatus.UNRECOGNIZED -> R.color.fairPink
        WithdrawStatus.PAID_OUT -> R.color.frostedMint
    }

const val CURRENCY_LENGTH = 8