package com.niteshkumarjha.rakutenapp

data class Product(
    val id: Long,
    val headline: String,
    val newBestPrice: Double,
    val usedBestPrice: Double,
    val imagesUrls: List<String>,
    val reviewsAverageNote: Double,
    val nbReviews: Int
)