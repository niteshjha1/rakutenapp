package com.niteshkumarjha.rakutenapp.ui.details

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.niteshkumarjha.rakutenapp.R
import com.niteshkumarjha.rakutenapp.viewmodel.ProductDetailsViewModel

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productImage: ImageView
    private lateinit var productTitle: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)

        productImage = findViewById(R.id.productImage)
        productTitle = findViewById(R.id.productTitle)
        productPrice = findViewById(R.id.productPrice)
        productDescription = findViewById(R.id.productDescription)
        progressBar = findViewById(R.id.progressBar)

        val productId = intent.getLongExtra("productId", -1)
        if (productId != -1L) {
            viewModel.getProductDetails(productId)
        }

        viewModel.productDetails.observe(this, Observer { productDetails ->
            hideProgressBar()
            productTitle.text = productDetails.headline
            productPrice.text = "New: \$${productDetails.newBestPrice}, Used: \$${productDetails.usedBestPrice}"
            productDescription.text = HtmlCompat.fromHtml(productDetails.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            Glide.with(this).load(productDetails.images[0].imagesUrls.entry[0].url).into(productImage)
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })

        viewModel.error.observe(this, Observer { error ->
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
