package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.CustomerAdapter
import com.example.helia.databinding.ActivityCustomerBinding
import com.example.helia.data.CurrentInvoice
import com.example.helia.data.UserGuidanceManager
import com.example.helia.model.Customer
import com.example.helia.network.RetrofitClient
import kotlinx.coroutines.launch

//class CustomerActivity : AppCompatActivity() {
class CustomerActivity : BaseActivity() {
    private lateinit var binding: ActivityCustomerBinding
    private val customers = mutableListOf<Customer>()
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvCustomers.layoutManager = LinearLayoutManager(this)

        adapter = CustomerAdapter(
            customers = customers,
//                onCustomerClick = { customer ->
            onPlusClick = { customer ->
                if (!UserGuidanceManager.isCustomerHintShown) {
                    Toast.makeText(
                        this,
                        "برای ایجاد فاکتور میتوانید بر روی نام مشتری نیز کلیک کنید",
                        Toast.LENGTH_LONG
                    ).show()
                    UserGuidanceManager.isCustomerHintShown = true
                }
                CurrentInvoice.customer = customer
                startActivity(Intent(this, InvoiceActivity::class.java))
            },
            onRowClick = { customer ->
                CurrentInvoice.customer = customer
                startActivity(Intent(this, InvoiceActivity::class.java))
            },
            onHistoryClick = { customer ->
                CurrentInvoice.customer = customer
                startActivity(Intent(this@CustomerActivity, InvoiceHistoryActivity::class.java))
            },
            onLongClickNewInvoice =
                { customer ->
                    // نمایش دیالوگ راهنما
                    androidx.appcompat.app.AlertDialog.Builder(this) // اگر در Fragment هستید بنویسید: requireContext()
                        .setTitle("راهنما")
                        .setMessage("برای ایجاد فاکتور جدید می‌توانید بر روی آیکون + و یا حتی بر روی سطر نام مشتری کلیک کنید.")
                        .setPositiveButton("متوجه شدم") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

        )

        binding.rvCustomers.adapter =
            adapter
        loadCustomers()
    }

    private fun loadCustomers() {
        lifecycleScope.launch {
            try {
                val result = RetrofitClient.api.getCustomers()
                if (result.success) {
                    customers.clear()
                    customers.addAll(result.data ?: emptyList())
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@CustomerActivity,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CustomerActivity,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}