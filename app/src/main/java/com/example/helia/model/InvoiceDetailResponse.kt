package com.example.helia.model

import android.R

data class InvoiceDetailResponse(

    val invoiceID: Long,

    val customerID: Int,

    val customerName: String,

    val userID: Int,

    val invoiceDate: String,

    val invoiceTime: String,

    val totalAmount: Long,

    val items: List<InvoiceDetail>

)