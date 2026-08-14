package com.example.helia.model

data class InvoiceRequest(

    val customerID: Int,

    val userID: Int,

    val totalAmount: Long,

    val items: List<InvoiceDetailRequest>

)