package com.example.helia.model

data class InvoiceDetail(

    val productID: Int,

    val productName: String,

    val quantity: Int,

    val price: Long,

    val totalAmount: Long

)