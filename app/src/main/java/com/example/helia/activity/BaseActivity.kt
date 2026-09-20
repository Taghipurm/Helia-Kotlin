package com.example.helia.activity

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.helia.R
import com.example.helia.data.CurrentInvoice
import com.example.helia.data.UserGuidanceManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatDelegate

abstract class BaseActivity : AppCompatActivity() {

    // =========================
    // تایمر نشست - ۱۰ دقیقه
    // =========================
    private val SESSION_TIMEOUT = 10 * 60 * 1000L
    private val handler = Handler(Looper.getMainLooper())

    private val logoutRunnable = Runnable {

        UserGuidanceManager.reset()

        CurrentInvoice.customer = null

        val intent = Intent(this, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

    private fun resetSessionTimer() {
        handler.removeCallbacks(logoutRunnable)
        handler.postDelayed(logoutRunnable, SESSION_TIMEOUT)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        resetSessionTimer()
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        resetSessionTimer()
    }

    override fun onPause() {
        super.onPause()
    }


    // =========================
    // منوی کشویی مشترک
    // =========================
    protected fun setupDrawerMenu(drawerLayout: DrawerLayout) {

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val btnCloseDrawer = findViewById<ImageButton>(R.id.btnCloseDrawer)
        val menuNewInvoice = findViewById<TextView>(R.id.menuNewInvoice)
        val menuInvoiceHistory = findViewById<TextView>(R.id.menuInvoiceHistory)
        val menuSettings = findViewById<TextView>(R.id.menuSettings)
        val menuAbout = findViewById<TextView>(R.id.menuAbout)
        val menuLogout = findViewById<TextView>(R.id.menuLogout)

        // باز کردن منو
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        // بستن منو
        btnCloseDrawer.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }


        // فاکتور جدید
        menuNewInvoice.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, CustomerActivity::class.java))
        }

        // سوابق/تصویر فاکتورها
        menuInvoiceHistory.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("راهنما")
                .setMessage(
                    "برای مشاهده سوابق/تصویر فاکتورها بایستی در صفحه ایجاد فاکتور جدید بر روی آیکون سابقه/History کلیک کنید."
                )
                .setPositiveButton("متوجه شدم") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        menuSettings.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // درباره برنامه
        menuAbout.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, AboutActivity::class.java))
        }


        // خروج
        menuLogout.setOnClickListener {

//            drawerLayout.closeDrawer(GravityCompat.START)
//            finishAndRemoveTask()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()

        }
    }


}
