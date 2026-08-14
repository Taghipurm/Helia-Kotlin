package com.example.helia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemCustomerBinding
import com.example.helia.model.Customer

class CustomerAdapter(

    private val customers: MutableList<Customer>,

    private val onCustomerClick: (Customer) -> Unit,

    private val onHistoryClick: (Customer) -> Unit

) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    inner class ViewHolder(

        val binding: ItemCustomerBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        // این تابع داخل ViewHolder قرار می‌گیرد
        fun bind(customer: Customer) {

            binding.txtCustomerName.text =
                customer.customerName

/*
            binding.txtMobile.text =
                customer.mobile
*/

            binding.btnNewInvoice.setOnClickListener {
                onCustomerClick(customer)
            }

            binding.root.setOnClickListener {

                onCustomerClick(customer)

            }

            binding.btnHistory.setOnClickListener {

                onHistoryClick(customer)

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemCustomerBinding.inflate(
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

        holder.bind(customers[position])

    }

    override fun getItemCount() =
        customers.size

}
