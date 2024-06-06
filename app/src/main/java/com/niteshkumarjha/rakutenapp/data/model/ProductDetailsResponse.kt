package com.niteshkumarjha.rakutenapp.data.model

import com.google.gson.annotations.SerializedName


data class ProductDetailsResponse(
    val productId: Long,
    val newBestPrice: Double,
    val usedBestPrice: Double,
    val headline: String,
    val description: String,
    val images: List<ProductImage>,
)

data class ProductImage(
    @SerializedName("imagesUrls")
    val imagesUrls: Entry
)

data class Entry(
    val entry: List<ImageUrl>
)

data class ImageUrl(
    val size: String,
    val url: String
)

