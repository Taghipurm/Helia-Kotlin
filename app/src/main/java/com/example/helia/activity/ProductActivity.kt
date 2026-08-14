package com.example.helia.activity

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.ProductAdapter
import com.example.helia.databinding.ActivityProductBinding
import com.example.helia.data.CurrentInvoice
import com.example.helia.model.Product
import com.example.helia.network.RetrofitClient
import kotlinx.coroutines.launch
class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    private val products =
        mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

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
        adapter =
            ProductAdapter(products) { product ->
                askQuantity(product)
            }
        adapter.notifyDataSetChanged()
        // 3- اتصال Adapter به RecyclerView
        binding.rvProducts.adapter =
            adapter
        // 4- دریافت کالاها از API
        loadProducts()
    }

    private fun askQuantity(product: Product) {

        val edt = EditText(this)

        edt.inputType =
            InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)

            .setTitle(product.productName)

            .setMessage("تعداد")

            .setView(edt)

            .setPositiveButton("تأیید") { _, _ ->

                val quantity =
                    edt.text.toString()
                        .toIntOrNull() ?: 1

                CurrentInvoice.addItem(
                    product,
                    quantity
                )

                finish()

            }

            .setNegativeButton("انصراف", null)

            .show()

    }

    private fun loadProducts() {

        lifecycleScope.launch {

            try {

                val result = RetrofitClient.api.getProducts()

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



}