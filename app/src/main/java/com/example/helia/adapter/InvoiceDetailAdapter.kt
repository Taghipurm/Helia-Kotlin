package com.example.helia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemInvoiceDetailBinding
import com.example.helia.model.InvoiceDetail

class InvoiceDetailAdapter(

    private val items: List<InvoiceDetail>

) : RecyclerView.Adapter<InvoiceDetailAdapter.ViewHolder>() {

    inner class ViewHolder(

        val binding: ItemInvoiceDetailBinding

    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemInvoiceDetailBinding.inflate(
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

        val item =
            items[position]

        holder.binding.txtProductName.text =
            item.productName

        holder.binding.txtQuantity.text =
            "تعداد: ${item.quantity}"

        holder.binding.txtPrice.text =
            "قیمت: ${String.format("%,d", item.price)}"

        holder.binding.txtRowTotal.text =
            "جمع: ${String.format("%,d", item.totalAmount)} ریال"

    }

    override fun getItemCount(): Int =
        items.size
}