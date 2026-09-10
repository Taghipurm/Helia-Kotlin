package com.example.helia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.helia.databinding.ItemCustomerBinding
import com.example.helia.model.Customer
import com.example.helia.model.Product

class CustomerAdapter(

    private val customers: MutableList<Customer>,

//    private val onCustomerClick: (Customer) -> Unit,

    private val onPlusClick: (Customer) -> Unit,

    private val onRowClick: (Customer) -> Unit,

    private val onHistoryClick: (Customer) -> Unit,

    private val onLongClickNewInvoice: (Customer) -> Unit

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
//                onCustomerClick(customer)
                onPlusClick(customer)
            }

            binding.btnNewInvoice.setOnLongClickListener {
                // 1. تولید لرزش کوچک برای حس بهتر کاربر (Haptic Feedback)
                it.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)

                // 2. اجرای دستور ارسال شده به آداپتور
                onLongClickNewInvoice(customer)

                true // یعنی رویداد مصرف شد و نباید کلیک معمولی همزمان اجرا شود
            }

            binding.root.setOnClickListener {
//                onCustomerClick(customer)
                onRowClick(customer)
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
