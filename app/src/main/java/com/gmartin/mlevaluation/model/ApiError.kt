package com.gmartin.mlevaluation.model

/**
 * Data class for response error from MercadoLibre API.
 *
 * @author Guillermo O. Martín
 */
data class ApiError(
    val message: String,
    val error: String,
    val status: Int
)
