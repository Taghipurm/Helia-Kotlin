package com.example.helia.activity

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.helia.data.CurrentInvoice
import com.example.helia.data.UserGuidanceManager

abstract class BaseActivity : AppCompatActivity() {

    // ۱۰ دقیقه به میلی‌ثانیه: 10 * 60 * 1000
    private val SESSION_TIMEOUT = 10 * 60 * 1000L
    private val handler = Handler(Looper.getMainLooper())

    private val logoutRunnable = Runnable {
        // ۱. پاک کردن وضعیت‌های راهنما
        UserGuidanceManager.reset()
        // ۲. سایر پاکسازی‌ها (اگر لازم است)
        // پاکسازی داده‌های حساس
        CurrentInvoice.customer = null
        // یا پاک کردن توکن از SharedPreferences
        // getSharedPreferences("Prefs", MODE_PRIVATE).edit().clear().apply()
        // اینجا به صفحه لاگین بروید
        val intent = Intent(this, LoginActivity::class.java)
        // برای پاک کردن تاریخچه صفحات قبلی تا کاربر نتواند با دکمه Back برگردد
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // متد اصلی برای بازنشانی تایمر
    private fun resetSessionTimer() {
        handler.removeCallbacks(logoutRunnable)
        handler.postDelayed(logoutRunnable, SESSION_TIMEOUT)
    }

    // این متد هر زمان که کاربر صفحه را لمس کند فراخوانی می‌شود
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
        // اگر می‌خواهید وقتی اپلیکیشن بسته است هم تایمر کار کند، این خط را حذف کنید
        // ولی معمولا بهتر است تایمر همیشه فعال باشد
    }
}
