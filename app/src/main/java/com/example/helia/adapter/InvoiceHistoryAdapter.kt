package com.example.helia.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemInvoiceHistoryBinding
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
                "فاکتور: ${invoice.invoiceID}"

//            binding.txtDateTime.text =
//                "${invoice.invoiceDate} ${invoice.invoiceTime}"
            binding.txtDateTime.text =
                "${invoice.invoiceDate}"

            binding.txtCount.text =
                "تعداد کالا: ${invoice.itemCount}"

            binding.txtReturnedCount.text =
                "ب.گ. ${invoice.itemReturnedCount}"

            binding.txtAmount.text =
                "${String.format("%,d", invoice.totalAmount)} ریال"

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