package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.InvoiceDetailAdapter
import com.example.helia.databinding.ActivityInvoiceDetailBinding
import com.example.helia.network.RetrofitClient
import kotlinx.coroutines.launch


class InvoiceDetailActivity : AppCompatActivity() {
    private lateinit var binding:
            ActivityInvoiceDetailBinding
    private lateinit var adapter:
            InvoiceDetailAdapter
    private var invoiceID: Long = 0
    private val editLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == RESULT_OK) {

                loadInvoice()

            }

        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityInvoiceDetailBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        invoiceID =
            intent.getLongExtra(
                "InvoiceID",
                0
            )

        binding.rvItems.layoutManager =
            LinearLayoutManager(this)

        binding.btnDelete.setOnClickListener {

            AlertDialog.Builder(this)

                .setTitle("حذف فاکتور")

                .setMessage("آیا از حذف این فاکتور مطمئن هستید؟")

                .setPositiveButton("بله") { _, _ ->

                    deleteInvoice()

                }

                .setNegativeButton("خیر", null)

                .show()

        }

        binding.btnEdit.setOnClickListener {


            val intent =
                Intent(
                    this,
                    InvoiceActivity::class.java
                )


            intent.putExtra(
                "MODE",
                "EDIT"
            )


            intent.putExtra(
                "InvoiceID",
                invoiceID
            )


            editLauncher.launch(intent)

        }

        loadInvoice()

    }

    private fun loadInvoice() {

        lifecycleScope.launch {

            try {

                Toast.makeText(applicationContext, "MY Point1", Toast.LENGTH_LONG).show()

                val result =
                    RetrofitClient.api.getInvoice(
                        invoiceID
                    )
                Toast.makeText(applicationContext, "MY Point1.1", Toast.LENGTH_LONG).show()

                if(result.success) {

                    val invoice =
                        result.data

                    if(invoice != null) {

                        //-----------------
                        // Header
                        //-----------------

                        binding.txtInvoiceID.text =
                            "فاکتور شماره: ${invoice.invoiceID}"

                        binding.txtCustomer.text =
                            "مشتری: ${invoice.customerName}"

//                        binding.txtDate.text =
//                            "${invoice.invoiceDate}   ${invoice.invoiceTime}"
                        binding.txtDate.text =
                            "${invoice.invoiceDate}"

                        //-----------------
                        // Details
                        //-----------------

                        adapter =
                            InvoiceDetailAdapter(
                                invoice.items
                            )

                        binding.rvItems.adapter =
                            adapter

                        //-----------------
                        // Total
                        //-----------------

                        binding.txtTotal.text =
                            "جمع کل: ${
                                String.format(
                                    "%,d",
                                    invoice.totalAmount
                                )
                            } ریال"

                    }

                }
                else {

                    Toast.makeText(

                        this@InvoiceDetailActivity,

                        result.message,

                        Toast.LENGTH_LONG

                    ).show()

                }

            }
            catch(e: Exception) {

                Toast.makeText(

                    this@InvoiceDetailActivity,

                    e.message ?: "خطای نامشخص1",

                    Toast.LENGTH_LONG

                ).show()

            }

        }

    }

    private fun deleteInvoice() {

        lifecycleScope.launch {

            try {

                Log.d("DELETE", "Deleting invoice $invoiceID")

                val result =
                    RetrofitClient.api.deleteInvoice(
                        invoiceID
                    )

                if (result.success) {

                    Toast.makeText(
                        this@InvoiceDetailActivity,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()

                    setResult(RESULT_OK)

                    finish()

                } else {

                    Toast.makeText(
                        this@InvoiceDetailActivity,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

            }
            catch (e: Exception) {

                Log.e("DELETE", e.toString())

                Toast.makeText(
                    this@InvoiceDetailActivity,
                    e.message ?: "خطای نامشخص2",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }



}