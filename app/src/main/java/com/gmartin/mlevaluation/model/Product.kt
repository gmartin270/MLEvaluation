package com.gmartin.mlevaluation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class of Product item.
 *
 * @author Guillermo O. Martín
 */
@Parcelize
data class Product(
    val id: String,
    val title: String,
    val thumbnail: String,
    val price: Double,
    @SerializedName("available_quantity") val availableQuantity: Int,
) : Parcelable
