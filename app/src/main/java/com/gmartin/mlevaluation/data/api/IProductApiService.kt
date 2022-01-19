package com.gmartin.mlevaluation.data.api

import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.model.ProductResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * This class represents the Product API, all endpoints can stay here.
 *
 * @author Guillermo O. Martín
 */
interface IProductApiService {

    /**
     * Gets all products that matches with the product pattern passed through.
     *
     * @param searchPattern A [String] value for the search pattern.
     * @return A [Response] instance with the [ProductResponseData] from the Mercado Libre endpoint.
     */
    @GET("sites/MLA/search?")
    suspend fun getProducts(@Query("q") searchPattern: String): Response<ProductResponseData>

    /**
     * Gets a [Product] item for the provided Id.
     *
     * @param productId The [Product] Id to be retrieved.
     * @return A [Response] instance of [Product] type.
     */
    @GET("items/{id}")
    suspend fun getProduct(@Path("id") productId: String): Response<Product>
}
