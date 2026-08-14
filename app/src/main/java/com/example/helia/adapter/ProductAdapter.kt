package com.example.helia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemProductBinding
import com.example.helia.model.Product

class ProductAdapter(
    private val items: List<Product>,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val product = items[position]
        holder.binding.txtName.text =
            product.productName
        holder.binding.txtPrice.text =
            product.price.toString()
        holder.binding.root.setOnClickListener {
            onClick(product)
        }
    }

    override fun getItemCount(): Int =
        items.size

}