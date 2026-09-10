package com.example.helia.model

data class InvoiceHistory(

//    val invoiceID: Long,
    val invoiceID: String,

    val invoiceDate: String,

//    val invoiceTime: String,

    val totalAmount: Long,

    val itemCount: Int,

    val itemReturnedCount: Int

)