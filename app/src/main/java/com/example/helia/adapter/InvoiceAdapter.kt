package com.example.helia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemInvoiceBinding
import com.example.helia.helpers.toPersianDigits
import com.example.helia.helpers.toPersianFormattedNumber
import com.example.helia.model.InvoiceItem

class InvoiceAdapter(
    private val items: MutableList<InvoiceItem>,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ItemInvoiceBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemInvoiceBinding.inflate(
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
        holder.binding.txtName.text = item.productName
        holder.binding.txtQty.text = item.quantity.toPersianDigits()
        holder.binding.txtItemReturned.text=
//            item.returnedQuantity.toString()
            "ب.گ.[${item.returnedQuantity.toPersianDigits()}]"
        holder.binding.txtPrice.text =
//            (item.price * item.quantity).toString()
            (item.price * (item.quantity-item.returnedQuantity)).toPersianFormattedNumber()

/*        // افزایش تعداد
        holder.binding.btnPlus.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            onChanged()
        }
        // کاهش تعداد
        holder.binding.btnMinus.setOnClickListener {
            if(item.quantity > 1) {
                item.quantity--
            }
            notifyItemChanged(position)
            onChanged()
        }
*/
        // حذف کالا
        holder.binding.btnDelete.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(
                position,
                items.size
            )
            onChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}