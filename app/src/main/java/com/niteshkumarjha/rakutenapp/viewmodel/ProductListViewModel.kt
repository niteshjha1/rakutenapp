package com.niteshkumarjha.rakutenapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niteshkumarjha.rakutenapp.data.model.Product
import com.niteshkumarjha.rakutenapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    fun searchProducts(keyword: String) {
        viewModelScope.launch {
            try {
                _showProgressBar.value = true
                val response = repository.searchProducts(keyword)
                _products.value = response.products
            } catch (e: Exception) {
                Log.e("NITESH_NITESH", "Error fetching product list", e)
            } finally {
                _showProgressBar.value = false
            }
        }
    }
}

