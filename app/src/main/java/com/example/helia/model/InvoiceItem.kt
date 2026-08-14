package com.example.helia.model

data class InvoiceItem(

    val productID: Int,

    val productName: String,

    val price: Long,

    var quantity: Int

)