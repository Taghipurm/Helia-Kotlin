package com.example.helia.model

import android.R

data class InvoiceDetailResponse(

//    val invoiceID: Long,
    val invoiceID: String,

//    val customerID: Int,
    val customerID: String,

    val customerName: String,

    val userID: Int,

    val invoiceDate: String,

//    val invoiceTime: String,

    val totalAmount: Long,

    val items: List<InvoiceDetail>

)