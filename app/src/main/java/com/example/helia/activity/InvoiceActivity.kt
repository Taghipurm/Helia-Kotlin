package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.InvoiceAdapter
import com.example.helia.data.PreferencesManager
import com.example.helia.data.CurrentInvoice
import com.example.helia.databinding.ActivityInvoiceBinding
import com.example.helia.model.Customer
import com.example.helia.model.InvoiceDetailRequest
import com.example.helia.model.InvoiceRequest
import com.example.helia.model.InvoiceItem
import com.example.helia.network.RetrofitClient
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.widget.TextView

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var adapter: InvoiceAdapter
    private var editMode = false
    private var editInvoiceID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fillInvoiceImageItems()

        editMode =
            intent.getStringExtra("MODE") == "EDIT"

        editInvoiceID =
            intent.getLongExtra(
                "InvoiceID",
                0
            )

        if (editMode) {
            title = "ویرایش فاکتور"
            lifecycleScope.launch {

                loadInvoiceForEdit()
            }
        } else {
            title = "ثبت فاکتور"
        }

        binding.txtCustomer.text =
            "مشتری: ${CurrentInvoice.customer?.customerName ?: ""}"
        binding.txtTotal.text =
            "جمع کل: ${CurrentInvoice.total()}"
        binding.rvItems.layoutManager =
            LinearLayoutManager(this@InvoiceActivity)
        adapter =
            InvoiceAdapter(
                CurrentInvoice.items
            ) {
                updateTotal()
                updateSaveButton()
            }
        binding.rvItems.adapter =
            adapter
        updateSaveButton()
        binding.btnAddProduct.setOnClickListener {
            startActivity(
                Intent(
                    this@InvoiceActivity,
                    ProductActivity::class.java
                )
            )
        }
        updateTotal()
        updateSaveButton()
        binding.btnSaveInvoice.setOnClickListener {
            lifecycleScope.launch {

                if (editMode) {
                    updateInvoice()
                } else {
                    saveInvoice()
                }

            }

        }
    }

    private fun updateTotal() {
        binding.txtTotal.text =
            "جمع کل: ${CurrentInvoice.total()}"
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        binding.txtTotal.text =
            "جمع کل: ${CurrentInvoice.total()}"
        updateSaveButton()
    }

    private fun updateSaveButton() {
        binding.btnSaveInvoice.isEnabled =
            CurrentInvoice.items.isNotEmpty()
    }

    private suspend fun saveInvoice() {

        try {

            val request = InvoiceRequest(

                customerID =
                    CurrentInvoice.customer?.customerID ?: 0,

                userID =
                    PreferencesManager.getUserID(this),

                totalAmount =
                    CurrentInvoice.total(),

                items =
                    CurrentInvoice.items.map {

                        InvoiceDetailRequest(

                            productID = it.productID,

                            quantity = it.quantity,

                            price = it.price,

                            totalAmount = it.price * it.quantity

                        )
                    }
            )

            val result =
                RetrofitClient.api.saveInvoice(request)

            if (result.success) {

                Toast.makeText(
                    this,
                    "فاکتور با شماره ${result.data} ثبت شد.",
                    Toast.LENGTH_LONG
                ).show()

                CurrentInvoice.clear()

                val intent =
                    Intent(
                        this,
                        CustomerActivity::class.java
                    )

                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK

                startActivity(intent)

                finish()

            } else {

                Toast.makeText(
                    this,
                    "API Error: ${result.message}",
                    Toast.LENGTH_LONG
                ).show()

            }

        } catch (e: Exception) {

            Toast.makeText(
                this,
                e.message ?: e.toString(),
                Toast.LENGTH_LONG
            ).show()

            e.printStackTrace()
        }
    }

    private suspend fun updateInvoice() {
        val request =
            InvoiceRequest(
                customerID =
                    CurrentInvoice.customer!!.customerID,
                userID =
                    PreferencesManager.getUserID(this),
                totalAmount =
                    CurrentInvoice.total(),
                items =
                    CurrentInvoice.items.map {
                        InvoiceDetailRequest(
                            productID =
                                it.productID,
                            quantity =
                                it.quantity,
                            price =
                                it.price,
                            totalAmount =
                                it.price *
                                        it.quantity
                        )
                    }
            )
        val result =
            RetrofitClient.api.updateInvoice(
                editInvoiceID,
                request
            )
        if (result.success) {
            Toast.makeText(
                this@InvoiceActivity,
                "ویرایش فاکتور با موفقیت انجام شد.",
                Toast.LENGTH_LONG
            ).show()
            CurrentInvoice.clear()

            setResult(RESULT_OK)

            finish()
        } else {
            Toast.makeText(
                this@InvoiceActivity,
                result.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private suspend fun loadInvoiceForEdit() {

        try {

            val result =
                RetrofitClient.api.getInvoice(
                    editInvoiceID
                )

            if (result.success) {

                val invoice =
                    result.data ?: return

                //---------------------------------
                // پاک کردن فاکتور قبلی
                //---------------------------------

                CurrentInvoice.clear()

                //---------------------------------
                // مشتری
                //---------------------------------

                CurrentInvoice.customer =
                    Customer(
                        customerID = invoice.customerID,
                        customerName = invoice.customerName,
                        mobile = "",
                        address = ""
                    )

                //---------------------------------
                // کالاها
                //---------------------------------

                invoice.items.forEach {

                    CurrentInvoice.items.add(

                        InvoiceItem(

                            productID = it.productID,

                            productName = it.productName,

                            quantity = it.quantity,

                            price = it.price

                        )

                    )

                }

                //---------------------------------
                // بروزرسانی صفحه
                //---------------------------------

                binding.txtCustomer.text =
                    "مشتری: ${invoice.customerName}"

                adapter.notifyDataSetChanged()

                updateTotal()

                updateSaveButton()

            }

        } catch (e: Exception) {

            Toast.makeText(
                this,
                e.message,
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun fillInvoiceImageItems() {

        binding.invoiceItemsContainer.removeAllViews()

        CurrentInvoice.items.forEach { item ->

            val row = LayoutInflater.from(this).inflate(
                R.layout.invoice_item_image,
                binding.invoiceItemsContainer,
                false
            )

            val txtName =
                row.findViewById<TextView>(R.id.txtItemName)

            val txtQuantity =
                row.findViewById<TextView>(R.id.txtItemQuantity)

            val txtPrice =
                row.findViewById<TextView>(R.id.txtItemPrice)

            val txtTotal =
                row.findViewById<TextView>(R.id.txtItemTotal)

            txtName.text = item.productName

            txtQuantity.text =
                item.quantity.toString()

            txtPrice.text =
                formatPrice(item.price)

            txtTotal.text =
                formatPrice(item.price * item.quantity)

            binding.invoiceItemsContainer.addView(row)
        }
    }

    private fun formatPrice(value: Long): String {

        return String.format(
            "%,d",
            value
        )
    }



}