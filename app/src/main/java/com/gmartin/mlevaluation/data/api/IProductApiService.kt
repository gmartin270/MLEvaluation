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
     * @param searchProduct A [String] value for the search pattern.
     * @return A [Response] instance with the [ProductResponseData] from the Mercado Libre endpoint.
     */
    @GET("sites/MLA/search?")
    suspend fun getProducts(@Query("q") searchProduct: String): Response<ProductResponseData>

    /**
     * TODO
     */
    @GET("items/{id}")
    suspend fun getProduct(@Path("id") id: String): Response<Product>
}
