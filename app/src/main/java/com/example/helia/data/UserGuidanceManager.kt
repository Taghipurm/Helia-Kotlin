package com.example.helia.data

object UserGuidanceManager {
    // این متغیر در کل برنامه در دسترس است و وضعیت را نگه می‌دارد
    var isProductHintShown: Boolean = false
    var isCustomerHintShown: Boolean = false
    // اضافه کردن این تابع برای ریست کردن وضعیت‌ها
    fun reset() {
        isProductHintShown = false
        isCustomerHintShown = false
    }
}
