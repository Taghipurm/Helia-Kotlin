package com.example.helia.activity

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.ProductAdapter
import com.example.helia.databinding.ActivityProductBinding
import com.example.helia.data.CurrentInvoice
import com.example.helia.data.UserGuidanceManager
import com.example.helia.model.Product
import com.example.helia.network.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

//class ProductActivity : AppCompatActivity() {
class ProductActivity : BaseActivity() {
    private lateinit var binding: ActivityProductBinding
    private val products =
        mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

//    private var isHintShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 1- تنظیم RecyclerView
        binding.rvProducts.layoutManager =
            LinearLayoutManager(this@ProductActivity)
        // 2- ساخت Adapter
        // 👇 اینجا قرار می‌گیرد
//        adapter =
//            ProductAdapter(products) { product ->
//                askQuantity(product)
//                showGuidanceSnackbar()
//            }
        adapter = ProductAdapter(
            products,
            onPlusClicked = { product ->
                askQuantity(product)
//                showGuidanceSnackbar()   // ✅ راهنما فقط اینجا
//                if (!isHintShown) {
                if (!UserGuidanceManager.isProductHintShown) {
                    Toast.makeText(
                        this,
//            "با کلیک بر روی سطر نام کالا نیز می تواند کالا را به فاکتور اضافه کنید",
                        "برای افزودن کالا می توانید بر روی نام کالا نیز کلیک کنید",
                        Toast.LENGTH_LONG
                    ).show()
//                    isHintShown = true
                    UserGuidanceManager.isProductHintShown = true
                }
            },
            onRowClicked = { product ->
                askQuantity(product)
                // ❌ هیچ Snackbar اینجا صدا زده نمی‌شود
            }
        )
        adapter.notifyDataSetChanged()
        // 3- اتصال Adapter به RecyclerView
        binding.rvProducts.adapter =
            adapter

        // 4- دریافت کالاها از API
        loadProducts()
    }

    private fun askQuantity(product: Product) {

        val edtQuantity = EditText(this)

        edtQuantity.inputType =
            InputType.TYPE_CLASS_NUMBER

        edtQuantity.hint = "تعداد فروش"


        val edtReturned = EditText(this)

        edtReturned.inputType =
            InputType.TYPE_CLASS_NUMBER

        edtReturned.hint = "تعداد برگشتی"
        val layout = LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.addView(edtQuantity)

        layout.addView(edtReturned)

        AlertDialog.Builder(this)

            .setTitle(product.productName)

            .setView(layout)

            .setPositiveButton("تأیید") { _, _ ->

                val quantity =
                    edtQuantity.text
                        .toString()
                        .toIntOrNull() ?: 1

                val returnedQuantity =
                    edtReturned.text
                        .toString()
                        .toIntOrNull() ?: 0

                if (returnedQuantity > quantity) {

                    Toast.makeText(
                        this,
                        "تعداد برگشتی نمی‌تواند بیشتر از تعداد فروش باشد.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@setPositiveButton
                }

                CurrentInvoice.addItem(
                    product,
                    quantity,
                    returnedQuantity
                )

                finish()

            }

            .setNegativeButton("انصراف", null)

            .show()

    }

    private fun loadProducts() {

        lifecycleScope.launch {

            try {

//                val result = RetrofitClient.api.getProducts()
                val selectedCustomerId = CurrentInvoice.customer!!.customerID
                val result = RetrofitClient.api.getProducts(selectedCustomerId)

                if (result.success) {

                    products.clear()

                    products.addAll(result.data ?: emptyList())

                    adapter.notifyDataSetChanged()

                } else {

                    Toast.makeText(
                        this@ProductActivity,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ProductActivity,
                    e.localizedMessage ?: "خطا در ارتباط با سرور",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }

    private fun showGuidanceSnackbar() {
//        val prefs = getSharedPreferences("HelpPrefs", MODE_PRIVATE)
//        val isHintShown = prefs.getBoolean("product_hint_shown", false)
//
////        if (!isHintShown) {
//            // نمایش اسنک‌بار
//            Snackbar.make(
//                binding.root, // یا root ویوی شما (مثلاً CoordinatorLayout یا RecyclerView)
//                "می‌دانستید؟ می‌توانید با کلیک روی نام کالا هم آن را اضافه کنید",
//                Snackbar.LENGTH_LONG
//            ).setAction("متوجه شدم") {
//                // وقتی کاربر روی دکمه کلیک کرد، آن را ذخیره کن که دیگر نشان نده
//                prefs.edit().putBoolean("product_hint_shown", true).apply()
//            }.show()
//
//            // اگر کاربر روی اکشن کلیک نکرد، باز هم بعد از مدتی نشان نده (اختیاری)
//            // یا می‌توانید اینجا هم ذخیره کنید تا فقط یک بار نمایش داده شود
//            prefs.edit().putBoolean("product_hint_shown", true).apply()
////        }
        Toast.makeText(
            this,
//            "با کلیک بر روی سطر نام کالا نیز می تواند کالا را به فاکتور اضافه کنید",
            "برای افزودن کالا می توانید بر روی نام کالا نیز کلیک کنید",
            Toast.LENGTH_LONG
        ).show()

    }


}