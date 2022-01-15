package com.gmartin.mlevaluation.utils

sealed class StatusData<out T> {
    abstract val data: T?
    abstract val message: String?

    class Loading<out T>(
        override val data: T? = null,
        override val message: String? = null
    ) : StatusData<T>()

    class Success<out T>(
        override val data: T?,
        override val message: String? = null
    ) : StatusData<T>()

    class Error<out T>(
        override val data: T? = null,
        override val message: String?
    ) : StatusData<T>()
}
