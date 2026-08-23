package com.example.helia.model

data class InvoiceDetail(

//    val productID: Int,
    val productID: String,

    val productName: String,

    val quantity: Int,
    val returnedQuantity: Int,
    val price: Long,

    val totalAmount: Long

)