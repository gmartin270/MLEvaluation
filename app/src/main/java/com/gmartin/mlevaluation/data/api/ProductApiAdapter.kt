package com.gmartin.mlevaluation.data.api

import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.model.exception.ApiException
import org.koin.core.component.KoinComponent

private const val HTTP_NOT_FOUND_CODE = 404
private const val UNKNOWN_EXCEPTION_TEXT = "Unknown exception"

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
     * @param searchPattern A [String] value for the search pattern.
     * @return A [Product] collection instance.
     */
    suspend fun getProductsList(searchPattern: String): List<Product> {
        val response = mProductApiClient.provideProductApi().getProducts(searchPattern)

        if (response.isSuccessful) {
            return response.body()!!.productList
        } else {
            val apiError = response.errorBody()?.let { mProductApiClient.parseApiError(it) }
            throw ApiException(
                apiError?.status ?: HTTP_NOT_FOUND_CODE,
                apiError?.message ?: UNKNOWN_EXCEPTION_TEXT
            )
        }
    }

    /**
     * Gets a [Product] item for the provided Id.
     *
     * @param productId The [Product] Id to be retrieved.
     * @return A [Product] instance.
     */
    suspend fun getProduct(productId: String): Product {
        val response = mProductApiClient.provideProductApi().getProduct(productId)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val apiError = response.errorBody()?.let { mProductApiClient.parseApiError(it) }
            throw ApiException(
                apiError?.status ?: HTTP_NOT_FOUND_CODE,
                apiError?.message ?: UNKNOWN_EXCEPTION_TEXT
            )
        }
    }
}
