package com.niteshkumarjha.rakutenapp

data class ProductDetailsResponse(
    val id: Long,
    val headline: String,
    val newBestPrice: Double,
    val usedBestPrice: Double,
    val imagesUrls: List<String>,
    val description: String,
    val score: Double,
    val nbReviews: Int
)