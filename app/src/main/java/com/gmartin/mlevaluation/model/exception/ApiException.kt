package com.gmartin.mlevaluation.model.exception

/**
 * Custom [Exception] class for API exceptions.
 *
 * @author Guillermo O. Martín
 */
class ApiException(val code: Int, override val message: String) : Exception(message)
