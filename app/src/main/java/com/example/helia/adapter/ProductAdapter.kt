package com.example.helia.adapter

import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemProductBinding
import com.example.helia.helpers.toPersianFormattedNumber
import com.example.helia.model.Product
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.snackbar.Snackbar

class ProductAdapter(
    private val items: List<Product>,
//    private val onClick: (Product) -> Unit,
    private val onPlusClicked: (Product) -> Unit,
    private val onRowClicked: (Product) -> Unit
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
        holder.binding.txtName.text = product.productName
        holder.binding.txtName.post {
            if (holder.binding.txtName.lineCount >= 2) {
                holder.binding.txtName.setPadding(
                    holder.binding.txtName.paddingLeft,
                    holder.binding.txtName.paddingTop,
                    holder.binding.txtName.paddingRight,
                    (4 * holder.binding.txtName.resources.displayMetrics.density).toInt()
                )
            } else {
                holder.binding.txtName.setPadding(
                    holder.binding.txtName.paddingLeft,
                    holder.binding.txtName.paddingTop,
                    holder.binding.txtName.paddingRight,
                    0
                )
            }
        }
        holder.binding.txtPrice.text = product.price.toPersianFormattedNumber()
//        holder.binding.root.setOnClickListener {
//            onClick(product)
//        }
        holder.binding.root.setOnClickListener {
            onRowClicked(product)
        }
//        holder.binding.imgBtnAdd.setOnClickListener {
//            onClick(product)
//        }
        holder.binding.imgBtnAdd.setOnClickListener {
            onPlusClicked(product)
        }


    }

    override fun getItemCount(): Int =
        items.size



}