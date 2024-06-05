package com.niteshkumarjha.rakutenapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var searchBar: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button

    private lateinit var productAdapter: ProductAdapter
    private val products = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NITESH_NITESH", "onCreate Called")

        searchBar = findViewById(R.id.searchBar)
        recyclerView = findViewById(R.id.recyclerView)
        searchButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val keyword = searchBar.text.toString()
            Log.d("NITESH_NITESH", "Search initiated with keyword: $keyword")
            searchProducts(keyword)
        }

        productAdapter = ProductAdapter(products) { product ->
            Log.d("NITESH_NITESH", "Product clicked: ${product.headline}")
            val intent = Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra("productId", product.id)
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapter

        searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = v.text.toString()
                Log.d("NITESH_NITESH", "Search initiated with keyword: $keyword")
                searchProducts(keyword)
                true
            } else {
                false
            }
        }

        searchBar.addTextChangedListener { text ->
            Log.d("NITESH_NITESH", "Text entered: ${text.toString()}")
        }
    }

    private fun searchProducts(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("NITESH_NITESH", "Making network call to search products with keyword: $keyword")
                val response = RetrofitClient.apiService.searchProducts(keyword)
                Log.d("NITESH_NITESH", "Network call successful, updating UI with products")
                withContext(Dispatchers.Main) {
                    products.clear()
                    products.addAll(response.products)
                    productAdapter.notifyDataSetChanged()
                    Log.d("NITESH_NITESH", "UI updated with ${products.size} products")
                    for (product in products) {
                        Log.d("NITESH_NITESH", "Product: ${product.headline}")
                    }
                }
            } catch (e: Exception) {
                Log.e("NITESH_NITESH", "Network call failed", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
