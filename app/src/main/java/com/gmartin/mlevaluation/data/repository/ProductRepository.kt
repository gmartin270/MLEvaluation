package com.gmartin.mlevaluation.data.repository

import com.gmartin.mlevaluation.data.api.ProductApiAdapter
import com.gmartin.mlevaluation.model.Product
import org.koin.core.component.KoinComponent

/**
 * Repository class for Products.
 *
 * @author Guillermo O. Martin
 */
class ProductRepository(private val productsApiAdapter: ProductApiAdapter) : KoinComponent {
    /**
     * Gets all products that matches with the search pattern.
     *
     * @param searchPattern A [String] value for the search pattern.
     * @return A [Product] collection instance.
     */
    suspend fun getProductsList(searchPattern: String) =
        productsApiAdapter.getProductsList(searchPattern)

    /**
     * Gets a [Product] item for the provided Id.
     *
     * @param productId The [Product] Id to be retrieved.
     * @return A [Product] instance.
     */
    suspend fun getProduct(productId: String) = productsApiAdapter.getProduct(productId)
}
