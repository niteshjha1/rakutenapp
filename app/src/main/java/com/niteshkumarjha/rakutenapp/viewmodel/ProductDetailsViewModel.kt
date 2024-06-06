package com.niteshkumarjha.rakutenapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niteshkumarjha.rakutenapp.data.model.ProductDetailsResponse
import com.niteshkumarjha.rakutenapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _productDetails = MutableLiveData<ProductDetailsResponse>()
    val productDetails: LiveData<ProductDetailsResponse>
        get() = _productDetails

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getProductDetails(productId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getProductDetails(productId)
                _productDetails.value = response
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = "Error fetching product details"
                Log.e("NITESH_NITESH", "Error fetching product details", e)
            }
        }
    }
}
