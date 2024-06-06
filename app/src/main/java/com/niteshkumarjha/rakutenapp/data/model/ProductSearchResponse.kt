package com.niteshkumarjha.rakutenapp.data.model

data class ProductSearchResponse(
    val totalResultProductsCount: Int,
    val resultProductsCount: Int,
    val pageNumber: Int,
    val title: String,
    val maxProductsPerPage: Int,
    val maxPageNumber: Int,
    val products: List<Product>
)