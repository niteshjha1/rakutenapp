package com.niteshkumarjha.rakutenapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val products: List<Product>, private val onItemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.productTitle)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val image: ImageView = itemView.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        Log.d("NITESH_NITESH", "Binding product: ${product.headline} at position: $position")
        holder.title.text = product.headline
        holder.price.text = "New: \$${product.newBestPrice}, Used: \$${product.usedBestPrice}"
        Glide.with(holder.image.context).load(product.imagesUrls.firstOrNull()).into(holder.image)
        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount() = products.size
}
