package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.R
import com.example.helia.adapter.InvoiceDetailAdapter
import com.example.helia.databinding.ActivityInvoiceDetailBinding
import com.example.helia.model.InvoiceDetailResponse
import com.example.helia.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import android.widget.TextView
import com.example.helia.helpers.toPersianDigits
import com.example.helia.helpers.toPersianFormattedNumber


//class InvoiceDetailActivity : AppCompatActivity() {
class InvoiceDetailActivity : BaseActivity() {
    private lateinit var binding:
            ActivityInvoiceDetailBinding
    private lateinit var adapter:
            InvoiceDetailAdapter

    //    private var invoiceID: Long = 0
    private var invoiceID: String = ""

    private lateinit var invoiceImageView: View

    private lateinit var loadedInvoice: InvoiceDetailResponse
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
            intent.getStringExtra("InvoiceID") ?: ""

        binding.rvItems.layoutManager =
            LinearLayoutManager(this)

        binding.btnDelete.setOnClickListener {

            AlertDialog.Builder(this)

                .setTitle("حذف فاکتور")

                .setMessage(
                    "آیا از حذف این فاکتور مطمئن هستید؟"
                )

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

        binding.btnInvoiceImage.setOnClickListener {
            showInvoiceImage()
        }

        loadInvoice()
    }

    private fun loadInvoice() {

        lifecycleScope.launch {

            try {

                val result = RetrofitClient.api.getInvoice(invoiceID)

                if (result.success) {

                    val invoice =
                        result.data

                    if (invoice != null) {

                        loadedInvoice = invoice

                        //-----------------
                        // Header
                        //-----------------

                        binding.txtInvoiceID.text =
                            "فاکتور شماره: ${invoice.invoiceID.toPersianDigits()}"

                        binding.txtCustomer.text =
                            "مشتری: ${invoice.customerName}"

//                        binding.txtDate.text =
//                            "${invoice.invoiceDate}   ${invoice.invoiceTime}"
                        binding.txtDate.text =
                            "${invoice.invoiceDate.toPersianDigits()}"

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
                            "جمع کل: ${invoice.totalAmount.toPersianFormattedNumber()} ریال"

                    }

                } else {

                    Toast.makeText(

                        this@InvoiceDetailActivity,

                        result.message,

                        Toast.LENGTH_LONG

                    ).show()

                }

            } catch (e: Exception) {

                Toast.makeText(

                    this@InvoiceDetailActivity,

                    e.message ?: "خطای نامشخص1",

                    Toast.LENGTH_LONG

                ).show()

            }

        }

    }

    private fun showInvoiceImage() {

        invoiceImageView =
            layoutInflater.inflate(
                R.layout.invoice_image,
                null
            )

        fillInvoiceImageInfo()

        fillInvoiceImageItems()

        AlertDialog.Builder(this)
            .setView(invoiceImageView)
            .setPositiveButton("بستن", null)
            .show()
    }

    private fun deleteInvoice() {

        lifecycleScope.launch {

            try {

//                Log.d("DELETE", "Deleting invoice $invoiceID")

                val result = RetrofitClient.api.deleteInvoice(invoiceID)

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

            } catch (e: Exception) {

                Log.e("DELETE", e.toString())

                Toast.makeText(
                    this@InvoiceDetailActivity,
                    e.message ?: "خطای نامشخص2",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }

    private fun fillInvoiceImageInfo() {

        val customer =
            invoiceImageView.findViewById<TextView>(
                R.id.txtInvoiceCustomer
            )

        val total =
            invoiceImageView.findViewById<TextView>(
                R.id.txtInvoiceTotal
            )

        val number =
            invoiceImageView.findViewById<TextView>(
                R.id.txtInvoiceNumber
            )

        val date =
            invoiceImageView.findViewById<TextView>(
                R.id.txtInvoiceDate
            )

//        number.text = "شماره2: ${loadedInvoice.invoiceID}"
        number.text = "شماره: ${loadedInvoice.invoiceID.toPersianDigits()}"


//        date.text =
//            "تاریخ2: ${loadedInvoice.invoiceDate}"
        date.text = "تاریخ: ${loadedInvoice.invoiceDate.toPersianDigits()}"

        customer.text =
            "نام مشتری: ${loadedInvoice.customerName}"

        total.text = "جمع کل: ${loadedInvoice.totalAmount.toPersianFormattedNumber()} ریال"
    }

    private fun fillInvoiceImageItems() {

        val container =
            invoiceImageView.findViewById<LinearLayout>(
                R.id.invoiceItemsContainer
            )

        container.removeAllViews()

        loadedInvoice.items.forEach { item ->

            val row =
                layoutInflater.inflate(
                    R.layout.invoice_item_image,
                    container,
                    false
                )

            val txtName =
                row.findViewById<TextView>(
                    R.id.txtItemName
                )

            val txtQuantity =
                row.findViewById<TextView>(
                    R.id.txtItemQuantity
                )

            val txtReturnedQuantity =
                row.findViewById<TextView>(
                    R.id.txtItemReturnedQuantity
                )

            val txtPrice =
                row.findViewById<TextView>(
                    R.id.txtItemPrice
                )

            val txtTotal =
                row.findViewById<TextView>(
                    R.id.txtItemTotal
                )

            txtName.text =item.productName

            txtQuantity.text = item.quantity.toPersianFormattedNumber()

            txtReturnedQuantity.text = item.returnedQuantity.toPersianDigits()

            txtPrice.text =(item.price).toPersianFormattedNumber()

            val netQuantity = item.quantity - item.returnedQuantity

            txtTotal.text = (item.price * netQuantity).toPersianFormattedNumber()

            container.addView(row)
        }
    }



}