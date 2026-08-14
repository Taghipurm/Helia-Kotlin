package com.example.helia.model

data class InvoiceDetailRequest(

    val productID: Int,

    val quantity: Int,

    val price: Long,

    val totalAmount: Long

)