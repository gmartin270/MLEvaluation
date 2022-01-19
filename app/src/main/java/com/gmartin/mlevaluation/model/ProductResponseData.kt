package com.gmartin.mlevaluation.model

import com.google.gson.annotations.SerializedName

/**
 * Data class of Product for the response from MercadoLibre API.
 *
 * @author Guillermo O. Martín
 */
data class ProductResponseData(
    @SerializedName("site_id") val siteId: String,
    @SerializedName("results") val productList: List<Product>
)
