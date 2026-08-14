package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.InvoiceHistoryAdapter
import com.example.helia.data.CurrentInvoice
import com.example.helia.databinding.ActivityInvoiceHistoryBinding
import com.example.helia.model.InvoiceHistory
import com.example.helia.network.RetrofitClient
import kotlinx.coroutines.launch

class InvoiceHistoryActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityInvoiceHistoryBinding

    private val invoices =
        mutableListOf<InvoiceHistory>()

    private lateinit var adapter:
            InvoiceHistoryAdapter

    private val invoiceDetailLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                loadInvoices()

            }

        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityInvoiceHistoryBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        supportActionBar?.title =
            "تاریخچه فاکتورها"

        adapter =
            InvoiceHistoryAdapter(invoices) { invoice ->

                val intent =
                    Intent(
                        this@InvoiceHistoryActivity,
                        InvoiceDetailActivity::class.java
                    )

                intent.putExtra(
                    "InvoiceID",
                    invoice.invoiceID
                )

                invoiceDetailLauncher.launch(intent)

            }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerView.adapter =
            adapter

        loadInvoices()

    }

    private fun loadInvoices() {

        val customerID =
            CurrentInvoice.customer?.customerID ?: return

        val customerName = CurrentInvoice.customer?.customerName

        lifecycleScope.launch {

            try {

                val result =
                    RetrofitClient.api.getInvoices(customerID)

                Log.d(
                    "INVOICE_HISTORY",
                    result.toString()
                )

                if (result.success) {

                    binding.txtCustomer.text =
                        "نام مشتری: ${customerName}"

                    invoices.clear()

                    result.data?.let {

                        invoices.addAll(it)

                    }

                    adapter.notifyDataSetChanged()

                } else {

                    Toast.makeText(
                        this@InvoiceHistoryActivity,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

            } catch (e: Exception) {

                Log.e(
                    "INVOICE_HISTORY",
                    e.toString()
                )

                Toast.makeText(
                    this@InvoiceHistoryActivity,
                    e.message ?: "خطای نامشخص3",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }

}