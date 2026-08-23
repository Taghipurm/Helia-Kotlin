package com.example.helia.model

import android.R

data class InvoiceRequest(

//    val customerID: Int,
    val customerID: String,

    val userID: Int,

    val totalAmount: Long,

    val items: List<InvoiceDetailRequest>

)