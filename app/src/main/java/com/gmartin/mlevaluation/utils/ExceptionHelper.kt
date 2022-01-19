package com.gmartin.mlevaluation.utils

import com.gmartin.mlevaluation.model.exception.ApiException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

/**
 * Helper class with utilities for exceptions.
 *
 * @author Guillermo O. Martín
 */
class ExceptionHelper {
    companion object {

        /**
         * Determines the [ErrorType] in base to an [Exception].
         *
         * @param exception An [Exception] instance.
         * @return An [ErrorType] value.
         */
        fun resolveError(exception: Exception): ErrorType {
            return when (exception) {
                is SocketTimeoutException,
                is ConnectException,
                is UnknownHostException -> ErrorType.NETWORK_EXCEPTION
                is HttpException, is ApiException -> ErrorType.API_EXCEPTION
                else -> ErrorType.GENERIC_EXCEPTION
            }
        }
    }
}
