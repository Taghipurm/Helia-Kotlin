package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helia.adapter.CustomerAdapter
import com.example.helia.data.CurrentInvoice
import com.example.helia.data.UserGuidanceManager
import com.example.helia.databinding.ActivityCustomerBinding
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


        // =========================
        // راه‌اندازی منوی مشترک
        // =========================
        setupDrawerMenu(binding.drawerLayout)


        // =========================
        // دکمه Back
        // =========================
//        binding.btnBackToLogin.setOnClickListener {
//            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                binding.drawerLayout.closeDrawer(GravityCompat.START)
//            } else {
//                onBackPressedDispatcher.onBackPressed()
//            }
//        }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        // =========================
        // RecyclerView مشتریان
        // =========================
        binding.rvCustomers.layoutManager =
            LinearLayoutManager(this)


        adapter = CustomerAdapter(

            customers = customers,

            // ایجاد فاکتور با دکمه +
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


            // ایجاد فاکتور با کلیک روی سطر مشتری
            onRowClick = { customer ->

                CurrentInvoice.customer = customer

                startActivity(Intent(this, InvoiceActivity::class.java))

            },


            // مشاهده تاریخچه فاکتور
            onHistoryClick = { customer ->

                CurrentInvoice.customer = customer

                startActivity(Intent(this@CustomerActivity, InvoiceHistoryActivity::class.java))

            },


            // کلیک طولانی برای راهنما
            onLongClickNewInvoice = { customer ->

                androidx.appcompat.app.AlertDialog.Builder(this)

                    .setTitle("راهنما")

                    .setMessage(
                        "برای ایجاد فاکتور جدید می‌توانید بر روی آیکون + و یا حتی بر روی سطر نام مشتری کلیک کنید."
                    )

                    .setPositiveButton("متوجه شدم") { dialog, _ ->

                        dialog.dismiss()
                    }

                    .show()
            }
        )


        binding.rvCustomers.adapter = adapter


        // =========================
        // دریافت مشتریان
        // =========================
        loadCustomers()
    }


    private fun loadCustomers() {

        lifecycleScope.launch {

            try {

                val result = RetrofitClient.api.getCustomers()

                if (result.success) {

                    customers.clear()

                    customers.addAll(
                        result.data ?: emptyList()
                    )

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