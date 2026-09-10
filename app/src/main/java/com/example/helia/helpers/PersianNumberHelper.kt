package com.example.helia.helpers

import java.util.Locale

/**
 * تبدیل ارقام انگلیسی به فارسی (برای متون، شماره‌ها و کدها)
 */
fun Any?.toPersianDigits(): String {
    if (this == null) return ""
    val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    return this.toString().map { ch ->
        if (ch in '0'..'9') persianDigits[ch - '0'] else ch
    }.joinToString("")
}

/**
 * جداسازی سه‌رقم‌سه‌رقم به همراه تبدیل ارقام به فارسی (مناسب برای مبالغ، قیمت‌ها و شماره فاکتور)
 */
fun Any?.toPersianFormattedNumber(): String {
    if (this == null) return ""
    val num = this.toString().toLongOrNull()
    val formatted = if (num != null) {
        String.format(Locale.US, "%,d", num)
    } else {
        this.toString()
    }
    return formatted.toPersianDigits()
}
