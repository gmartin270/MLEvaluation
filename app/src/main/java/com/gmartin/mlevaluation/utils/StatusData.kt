package com.gmartin.mlevaluation.utils

sealed class StatusData<out T> {
    abstract val data: T?
    abstract val errorType: ErrorType?
    abstract val message: String?

    class Loading<out T>(
        override val data: T? = null,
        override val errorType: ErrorType? = null,
        override val message: String? = null
    ) : StatusData<T>()

    class Success<out T>(
        override val data: T?,
        override val errorType: ErrorType? = null,
        override val message: String? = null
    ) : StatusData<T>()

    class Error<out T>(
        override val data: T? = null,
        override val errorType: ErrorType?,
        override val message: String? = null
    ) : StatusData<T>()
}
