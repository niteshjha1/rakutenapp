package com.niteshkumarjha.rakutenapp.data.network

import com.niteshkumarjha.rakutenapp.data.model.ProductDetailsResponse
import com.niteshkumarjha.rakutenapp.data.model.ProductSearchResponse
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

interface ApiService {
    @GET("products/search")
    suspend fun searchProducts(@Query("keyword") keyword: String): ProductSearchResponse

    @GET("products/details")
    suspend fun getProductDetails(@Query("id") productId: Long): ProductDetailsResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://4206121f-64a1-4256-a73d-2ac541b3efe4.mock.pstmn.io/"

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(CustomLoggingInterceptor())
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class CustomLoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        println("Sending request ${request.url()} on ${chain.connection()} \n ${request.headers()}")

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        println("Received response for ${response.request().url()} in ${(t2 - t1) / 1e6} ms \n ${response.headers()}")

        return response
    }
}