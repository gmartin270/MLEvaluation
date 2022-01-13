package com.gmartin.mlevaluation.utils

/**
 * Data class for the status
 */
data class StatusData<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T): StatusData<T> = StatusData(
            status = Status.SUCCESS,
            data = data,
            message = null
        )

        fun <T> error(data: T?, message: String): StatusData<T> = StatusData(
            status = Status.ERROR,
            data = data,
            message = message
        )

        fun <T> loading(data: T?): StatusData<T> = StatusData(
            status = Status.LOADING,
            data = data,
            message = null
        )
    }
}
