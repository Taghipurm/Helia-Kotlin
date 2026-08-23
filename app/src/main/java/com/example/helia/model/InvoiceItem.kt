package com.example.helia.model

data class InvoiceItem(
//    val productID: Int,
    val productID: String,
    val productName: String,
    val price: Long,
    var quantity: Int,
    var returnedQuantity: Int = 0
)