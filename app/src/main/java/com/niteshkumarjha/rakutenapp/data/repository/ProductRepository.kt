package com.niteshkumarjha.rakutenapp.data.repository

import com.niteshkumarjha.rakutenapp.data.network.RetrofitClient

class ProductRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun searchProducts(keyword: String) = apiService.searchProducts(keyword)

    suspend fun getProductDetails(productId: Long) = apiService.getProductDetails(productId)
}
