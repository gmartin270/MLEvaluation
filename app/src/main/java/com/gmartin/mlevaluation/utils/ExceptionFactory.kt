package com.gmartin.mlevaluation.utils

import android.util.Log
import com.gmartin.mlevaluation.model.ApiException
import com.gmartin.mlevaluation.model.NetworkException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionFactory {
    companion object {
        fun resolveError(e: Exception): Exception {
            var error = e
            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkException("Connection error!")
                }
                is ConnectException -> {
                    error = NetworkException("no internet access!")
                }
                is UnknownHostException -> {
                    error = NetworkException("no internet access!")
                }
                is HttpException -> {
                    error = ApiException(e.code(), "http error!")
                }
            }

            return error
        }
    }
}