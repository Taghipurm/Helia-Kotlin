package com.example.helia.activity

import android.content.ActivityNotFoundException
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
import android.view.View
import android.widget.TextView
import com.example.helia.R
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.os.Handler
import android.os.Looper

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding
    private lateinit var adapter: InvoiceAdapter

    private lateinit var invoiceImageView: View
    private var editMode = false
    private var editInvoiceID: Long = 0
    //    private var invoiceNumber: Long = 0
    private var invoiceNumber: String = "0"
    private var invoiceDate: String = ""
//    private var invoiceTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityInvoiceBinding.inflate(layoutInflater)

        setContentView(binding.root)

        editMode =
            intent.getStringExtra("MODE") == "EDIT"

        editInvoiceID =
            intent.getLongExtra(
                "InvoiceID",
                0
            )

        if (!editMode) {
            setCurrentInvoiceDateTime()
        }

        invoiceImageView = layoutInflater.inflate(
            R.layout.invoice_image,
            null
        )

//        editMode =
//            intent.getStringExtra("MODE") == "EDIT"
//
//        editInvoiceID =
//            intent.getLongExtra(
//                "InvoiceID",
//                0
//            )

        if (editMode) {
            title = "ویرایش فاکتور"
            lifecycleScope.launch {

                loadInvoiceForEdit()
            }
        } else {
            title = "ثبت فاکتور جدید2"
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
//                    CurrentInvoice.customer?.customerID ?: 0,
                CurrentInvoice.customer?.customerID ?: "0",

                userID =
                    PreferencesManager.getUserID(this),

                totalAmount =
                    CurrentInvoice.total(),

                items =
                    CurrentInvoice.items.map {
                        Log.d(
                            "INVOICE_RETURN",
                            "product=${it.productName}, quantity=${it.quantity}, returned=${it.returnedQuantity}"
                        )
                        InvoiceDetailRequest(
                            productID = it.productID,
                            productName = it.productName,
                            quantity = it.quantity,
                            returnedQuantity = it.returnedQuantity,
                            price = it.price,
                            totalAmount = it.price * (it.quantity - it.returnedQuantity)
                        )
                    }
            )

            val result =
                RetrofitClient.api.saveInvoice(request)

            if (result.success) {

//                invoiceNumber = result.data ?: 0
                invoiceNumber = result.data ?: "0"

                fillInvoiceImageInfo()
                fillInvoiceImageItems()

                val invoiceBitmap = createInvoiceBitmap()

                val invoiceUri =
                    saveInvoiceBitmap(invoiceBitmap)

                if (invoiceUri != null) {

                    Handler(Looper.getMainLooper()).postDelayed({

                        val intent = Intent(Intent.ACTION_VIEW).apply {

                            setDataAndType(
                                invoiceUri,
                                "image/*"
                            )

                            addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        }

                        try {

                            startActivity(intent)

                        } catch (e: ActivityNotFoundException) {

                            Toast.makeText(
                                this,
                                "برنامه‌ای برای نمایش تصویر پیدا نشد.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }, 1000)

                } else {

                    Log.e(
                        "INVOICE_IMAGE",
                        "invoiceUri IS NULL"
                    )
                }

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
                            productName = it.productName,
                            quantity =
                                it.quantity,
                            returnedQuantity = it.returnedQuantity,
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

                invoiceNumber = invoice.invoiceID
                invoiceDate = invoice.invoiceDate
//                invoiceTime = invoice.invoiceTime

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

        val container =
            invoiceImageView.findViewById<LinearLayout>(
                R.id.invoiceItemsContainer
            )

        container.removeAllViews()

        CurrentInvoice.items.forEach { item ->

            val row = layoutInflater.inflate(
                R.layout.invoice_item_image,
                container,
                false
            )

            val txtName =
                row.findViewById<TextView>(R.id.txtItemName)

            val txtQuantity =
                row.findViewById<TextView>(R.id.txtItemQuantity)

            val txtReturnedQuantity =
                row.findViewById<TextView>(R.id.txtItemReturnedQuantity)

            val txtPrice =
                row.findViewById<TextView>(R.id.txtItemPrice)

            val txtTotal =
                row.findViewById<TextView>(R.id.txtItemTotal)

            txtName.text = item.productName

            txtQuantity.text =
                item.quantity.toString()

            txtReturnedQuantity.text =
//                "برگشت: ${item.returnedQuantity}"
                item.returnedQuantity.toString()

            txtPrice.text =
                formatPrice(item.price)

            val netQuantity =
                item.quantity - item.returnedQuantity

            txtTotal.text =
                formatPrice(item.price * netQuantity)

            container.addView(row)
        }
    }

    private fun formatPrice(value: Long): String {
        return String.format("%,d", value)
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

//        val time =
//            invoiceImageView.findViewById<TextView>(
//                R.id.txtInvoiceTime
//            )

        number.text =
//            "شماره فاکتور:\n$invoiceNumber"
            "شماره: $invoiceNumber"

        date.text =
            "تاریخ: $invoiceDate"

//        time.text =
//            "ساعت: $invoiceTime"

        customer.text =
            "نام مشتری: ${CurrentInvoice.customer?.customerName ?: ""}"

        total.text =
            "جمع کل: ${formatPrice(CurrentInvoice.total())} ریال"


    }

    private fun setCurrentInvoiceDateTime() {

        val now = Date()

        val dateFormat =
            SimpleDateFormat(
                "yyyy/MM/dd",
                Locale.getDefault()
            )

        val timeFormat =
            SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            )

//        invoiceDate = dateFormat.format(now)
//        invoiceTime = timeFormat.format(now)

        // فرض کنیم این داخل onCreate اکتیویتی شماست
        lifecycleScope.launch {
            try {
                // حالا می‌توانید تابع suspend را فراخوانی کنید
                val response = RetrofitClient.api.getServerShamsiDate()

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.success) {
                        // نمایش تاریخ در UI
                        invoiceDate = data.date
                    }
                }
            } catch (e: Exception) {
                // مدیریت خطا (مثلاً قطع بودن اینترنت)
                e.printStackTrace()
            }
        }

    }

    private fun createInvoiceBitmap(): Bitmap {

        try {

            val view = invoiceImageView

            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    resources.displayMetrics.widthPixels,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                    0,
                    View.MeasureSpec.UNSPECIFIED
                )
            )

            view.layout(
                0,
                0,
                view.measuredWidth,
                view.measuredHeight
            )

            val bitmap = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)

            canvas.drawColor(Color.WHITE)

            view.draw(canvas)

            return bitmap

        } catch (e: Exception) {

            Log.e(
                "INVOICE_IMAGE",
                "Bitmap error: ${e.message}",
                e
            )

            throw e
        }

    }

    private fun saveInvoiceBitmap(
        bitmap: Bitmap
    ): Uri? {

        val fileName =
            "Invoice_$invoiceNumber.png"

        val values =
            ContentValues().apply {

                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    fileName
                )

                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/png"
                )

                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES +
                            "/HeliaInvoices"
                )

                put(
                    MediaStore.Images.Media.IS_PENDING,
                    1
                )
            }

        val resolver = contentResolver

        val uri =
            resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            ) ?: return null

        try {

            resolver.openOutputStream(uri).use { outputStream ->

                if (outputStream == null) {
                    return null
                }

                val success =
                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        outputStream
                    )

                if (!success) {
                    throw IllegalStateException(
                        "Bitmap compression failed"
                    )
                }
            }

            val completedValues =
                ContentValues().apply {

                    put(
                        MediaStore.Images.Media.IS_PENDING,
                        0
                    )
                }

            resolver.update(
                uri,
                completedValues,
                null,
                null
            )

            return uri

        } catch (e: Exception) {

            resolver.delete(
                uri,
                null,
                null
            )

            throw e
        }
    }


}