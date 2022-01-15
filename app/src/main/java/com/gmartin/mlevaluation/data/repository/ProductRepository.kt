package com.gmartin.mlevaluation.data.repository

import com.gmartin.mlevaluation.data.api.ProductApiAdapter
import org.koin.core.component.KoinComponent

/**
 * TODO
 */
class ProductRepository(private val productsApiAdapter: ProductApiAdapter) : KoinComponent {
    /**
     * TODO
     */
    suspend fun getProducts(search: String) = productsApiAdapter.getProducts(search)

    /**
     * TODO
     */
    suspend fun getProduct(itemId: String) = productsApiAdapter.getProduct(itemId)
}
