package com.cryptoexchange.mobile.core.base

import com.cryptoexchange.source.entity.UnauthorizedException
import com.cryptoexchange.source.entity.error.FailedRequestException
import com.cryptoexchange.source.entity.error.UnhandledErrResponseException
import com.cryptoexchange.source.entrypoint.SdkEntryPoint
import com.cryptoexchange.mobile.BuildConfig
import com.cryptoexchange.mobile.R
import com.cryptoexchange.mobile.core.global.ResourceManager
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val resourceManager: ResourceManager
) {

    fun handleError(exception: Throwable, messageListener: (String) -> Unit = {}) {
        when (exception) {
            is TimeoutException -> {
                messageListener(resourceManager.getString(R.string.timeout_error))
            }
            is UnknownHostException -> {
                messageListener(resourceManager.getString(R.string.network_error))
            }
            is UnauthorizedException -> {
                messageListener(resourceManager.getString(R.string.unauthorized_error))
                SdkEntryPoint.dependencyManager.userManager.unauthorized()
            }
            is FailedRequestException -> {
                messageListener(exception.message)
            }
            is UnhandledErrResponseException -> {
                val message = if (BuildConfig.DEBUG) {
                    exception.message
                } else {
                    resourceManager.getString(R.string.unknown_error)
                }
                messageListener(message)
            }
            else -> {
                messageListener(resourceManager.getString(R.string.unknown_error))
            }
        }
    }
}
