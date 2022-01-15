package com.gmartin.mlevaluation.model

class NetworkException(override val message: String): Exception(message)

class ApiException(val code: Int, override val message: String): Exception(message)