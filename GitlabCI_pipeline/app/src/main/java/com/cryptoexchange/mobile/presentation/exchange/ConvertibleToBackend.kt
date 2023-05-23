package com.cryptoexchange.mobile.presentation.exchange

interface ConvertibleToBackend<T> {

    val convertedBackendValue: T
}
