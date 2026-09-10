package com.example.helia.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemInvoiceHistoryBinding
import com.example.helia.helpers.toPersianDigits
import com.example.helia.helpers.toPersianFormattedNumber
import com.example.helia.model.InvoiceHistory


class InvoiceHistoryAdapter(
    private val invoices: List<InvoiceHistory>,
    private val onClick: (InvoiceHistory) -> Unit
) : RecyclerView.Adapter<InvoiceHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemInvoiceHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(invoice: InvoiceHistory) {

            binding.txtInvoiceID.text =
                "فاکتور: ${invoice.invoiceID.toPersianDigits()}"

//            binding.txtDateTime.text =
//                "${invoice.invoiceDate} ${invoice.invoiceTime}"
            binding.txtDateTime.text =
                "${invoice.invoiceDate.toPersianDigits()}"

            binding.txtCount.text =
                "تعداد کالا: ${invoice.itemCount.toPersianFormattedNumber()}"

//            binding.txtReturnedCount.text =
//                "ب.گ.[${invoice.itemReturnedCount}]"
            binding.txtReturnedCount.text = ""

            binding.txtAmount.text = "${invoice.totalAmount.toPersianFormattedNumber()} ریال"

            binding.root.setOnClickListener {

                onClick(invoice)

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemInvoiceHistoryBinding.inflate(
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

        holder.bind(invoices[position])

    }

    override fun getItemCount(): Int =
        invoices.size
}