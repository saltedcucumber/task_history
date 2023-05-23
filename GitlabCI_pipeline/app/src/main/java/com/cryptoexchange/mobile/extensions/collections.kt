package com.cryptoexchange.mobile.extensions

fun <T> MutableCollection<T>.clearAndAddAll(collection: Collection<T>) {
    clear()
    addAll(collection)
}
