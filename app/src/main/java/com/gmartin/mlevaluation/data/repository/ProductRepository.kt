package com.gmartin.mlevaluation.data.repository

import com.gmartin.mlevaluation.data.api.ProductApiAdapter
import org.koin.core.component.KoinComponent

class ProductRepository(private val productsApiAdapter: ProductApiAdapter) : KoinComponent {
    suspend fun getProducts(search: String) = productsApiAdapter.getProducts(search)
}
