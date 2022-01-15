package com.gmartin.mlevaluation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

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
    @SerializedName("pictures") val pictureList: @RawValue List<Picture>
) : Parcelable
