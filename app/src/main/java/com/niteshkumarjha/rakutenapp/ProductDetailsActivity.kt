package com.niteshkumarjha.rakutenapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productImage: ImageView
    private lateinit var productTitle: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productImage = findViewById(R.id.productImage)
        productTitle = findViewById(R.id.productTitle)
        productPrice = findViewById(R.id.productPrice)
        productDescription = findViewById(R.id.productDescription)

        val productId = intent.getLongExtra("productId", -1)
        if (productId != -1L) {
            Log.d("NITESH_NITESH", "Loading product details for product ID: $productId")
            loadProductDetails(productId)
        } else {
            Log.e("NITESH_NITESH", "Invalid product ID received")
        }
    }

    private fun loadProductDetails(productId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("NITESH_NITESH", "Making network call to fetch product details for product ID: $productId")
                val response = RetrofitClient.apiService.getProductDetails(productId)
                Log.d("NITESH_NITESH", "Network call successful, updating UI with product details")
                withContext(Dispatchers.Main) {
                    productTitle.text = response.headline
                    productPrice.text = "New: \$${response.newBestPrice}, Used: \$${response.usedBestPrice}"
                    productDescription.text = HtmlCompat.fromHtml(response.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    if (response.imagesUrls.isNullOrEmpty()) {
                        Log.e("NITESH_NITESH", "No image URL available")
                    } else {
                        Glide.with(this@ProductDetailsActivity).load(response.imagesUrls.first()).into(productImage)
                    }
                }
            } catch (e: Exception) {
                Log.e("NITESH_NITESH", "Network call failed", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductDetailsActivity, "Failed to fetch product details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
