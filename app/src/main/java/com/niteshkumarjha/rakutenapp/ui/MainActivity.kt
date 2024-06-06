package com.niteshkumarjha.rakutenapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.niteshkumarjha.rakutenapp.R
import com.niteshkumarjha.rakutenapp.data.model.Product
import com.niteshkumarjha.rakutenapp.ui.adapter.ProductAdapter
import com.niteshkumarjha.rakutenapp.ui.details.ProductDetailsActivity
import com.niteshkumarjha.rakutenapp.viewmodel.ProductListViewModel

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var searchBar: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var progressBar: ProgressBar
    private var popupWindow: PopupWindow? = null
    private lateinit var viewModel: ProductListViewModel
    private lateinit var productAdapter: ProductAdapter
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupViewModel()
        setupSearchBar()

        // Restore the search text if available
        savedInstanceState?.let {
            searchBar.setText(it.getString("searchText"))
        }

        // Handle the initial state of the empty view and popup window
        if (searchBar.text.isNullOrEmpty()) {
            toggleEmptyView(true)
            hidePopupWindow()
        } else {
            toggleEmptyView(false)
        }
    }

    private fun initViews() {
        searchBar = findViewById(R.id.searchBar)
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        progressBar = findViewById(R.id.progressBar)
        hidePopupWindow()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter({ product: Product ->
            val intent = Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra("productId", product.id)
            }
            startActivity(intent)
        }, mutableListOf<Product>())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)

        // Observe product list changes
        viewModel.products.observe(this, Observer { products ->
            productAdapter.updateProducts(products)
            toggleEmptyView(products.isEmpty())
        })

        // Observe progress bar visibility changes
        viewModel.showProgressBar.observe(this, Observer { showProgressBar ->
            toggleProgressBar(showProgressBar)
        })
    }

    private fun setupSearchBar() {
        searchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = v.text.toString()
                if (keyword.isNotEmpty()) {
                    viewModel.searchProducts(keyword)
                    hideKeyboard()
                }
                true
            } else {
                false
            }
        }

        searchBar.addTextChangedListener { text ->
            Log.d(TAG, "Text entered: ${text.toString()}")
            if (text.isNullOrEmpty()) {
                Log.d(TAG, "Search bar is empty, clearing RecyclerView")
                clearRecyclerView()
                hidePopupWindow()
            } else {
                if (searchBar.hasFocus()) {
                    handler.postDelayed({
                        if (!isFinishing && !isDestroyed) {
                            showPopupWindow()
                        }
                    }, 100)
                }
            }
        }

        searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchBar.text.isNotEmpty()) {
                handler.postDelayed({
                    if (!isFinishing && !isDestroyed) {
                        showPopupWindow()
                    }
                }, 100)
                Log.d(TAG, "Search bar focused with text, showing clear screen hint")
            } else {
                hidePopupWindow()
                Log.d(TAG, "Search bar not focused or empty, hiding clear screen hint")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", searchBar.text.toString())
    }

    private fun clearRecyclerView() {
        productAdapter.clearProducts()
        Log.d(TAG, "RecyclerView cleared")
        toggleEmptyView(true)
    }

    private fun toggleEmptyView(show: Boolean) {
        if (show) {
            recyclerView.visibility = RecyclerView.GONE
            emptyView.visibility = TextView.VISIBLE
            Log.d(TAG, "RecyclerView is empty, showing empty view")
        } else {
            recyclerView.visibility = RecyclerView.VISIBLE
            emptyView.visibility = TextView.GONE
            Log.d(TAG, "RecyclerView has items, hiding empty view")
        }
    }

    private fun toggleProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            Log.d(TAG, "Showing progress bar")
        } else {
            progressBar.visibility = View.GONE
            Log.d(TAG, "Hiding progress bar")
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
        Log.d(TAG, "Keyboard hidden")
    }

    private fun showPopupWindow() {
        if (popupWindow == null && !isFinishing && !isDestroyed) {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.popup_hint, null)

            searchBar.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val searchBarWidth = searchBar.measuredWidth
            val xOff = searchBarWidth - popupView.width

            popupWindow = PopupWindow(popupView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            popupWindow?.isFocusable = false

            // Show the popup window at the calculated position
            popupWindow?.showAsDropDown(searchBar, xOff, 10)
            Log.d(TAG, "Popup window shown")
        }
    }

    private fun hidePopupWindow() {
        popupWindow?.dismiss()
        popupWindow = null
        Log.d(TAG, "Popup window hidden")
    }
}