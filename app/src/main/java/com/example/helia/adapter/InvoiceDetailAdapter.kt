package com.example.helia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.R
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
        val item = items[position]

        holder.binding.txtProductName.text =
            item.productName

        holder.binding.txtQuantity.text =
            "تعداد: ${item.quantity}    برگشت: [${item.returnedQuantity}]"

        holder.binding.txtPrice.text =
            "قیمت: ${String.format("%,d", item.price)}"

        val netQuantity =
            item.quantity - item.returnedQuantity

        val rowTotal =
            item.price * netQuantity

        holder.binding.txtRowTotal.text =
            "جمع: ${String.format("%,d", rowTotal)} ریال"
    }

    override fun getItemCount(): Int =
        items.size

//    private fun getReturnedQuantity(
//        itemView: View
//    ): Int {
//
//        val editText =
//            itemView.findViewById<EditText>(
//                R.id.edtReturnedQuantity
//            )
//
//        return editText.text
//            .toString()
//            .trim()
//            .toIntOrNull()
//            ?: 0
//    }


}