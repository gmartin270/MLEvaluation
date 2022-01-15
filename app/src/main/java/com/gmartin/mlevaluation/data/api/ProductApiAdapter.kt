package com.gmartin.mlevaluation.data.api

import android.util.Log
import com.gmartin.mlevaluation.model.ApiException
import com.gmartin.mlevaluation.model.NetworkException
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.utils.ExceptionFactory
import org.koin.core.component.KoinComponent
import retrofit2.HttpException

/**
 * @author Guillermo O. Martín
 *
 * Adapter for responses to requests to the MercadoLibre product API.
 *
 * @param mProductApiClient A [ProductApiClient] instance.
 */
class ProductApiAdapter(private val mProductApiClient: ProductApiClient) : KoinComponent {

    /**
     * Gets all products that matches with the search pattern at MercadoLibre and adapts the
     * response to the domain model of [Product] list.
     *
     * @param searchProduct A [String] value for the search pattern.
     * @return A [Product] collection instance.
     */
    suspend fun getProducts(searchProduct: String): List<Product> {
        val response = mProductApiClient.provideProductApi().getProducts(searchProduct)

        try {
            if (response.isSuccessful) {
                return response.body()!!.productList
            } else {
                val apiError = response.errorBody()?.let { mProductApiClient.parseApiError(it) }
                throw ApiException(
                    apiError?.status ?: 404,
                    apiError?.message ?: "Unknown exception"
                )
            }
        } catch (exception: Exception) {
            throw ExceptionFactory.resolveError(exception)
        }
    }

    /**
     * TODO
     */
    suspend fun getProduct(productId: String): Product {
        val response = mProductApiClient.provideProductApi().getProduct(productId)

        try {
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                val apiError = response.errorBody()?.let { mProductApiClient.parseApiError(it) }
                throw ApiException(
                    apiError?.status ?: 404,
                    apiError?.message ?: "Unknown exception"
                )
            }
        } catch (exception: Exception) {
            throw ExceptionFactory.resolveError(exception)
        }
    }
}
