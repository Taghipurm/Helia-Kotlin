package com.example.helia.data

import com.example.helia.model.Customer
import com.example.helia.model.Product
import com.example.helia.model.InvoiceItem

object CurrentInvoice {
    // مشتری انتخاب شده
    var customer: Customer? = null
    // اقلام فاکتور
    val items = mutableListOf<InvoiceItem>()

    fun addItem(product: Product, quantity: Int) {
        val item = items.find { it.productID == product.productID }

        if (item == null) {
            items.add(
                InvoiceItem(
                    productID = product.productID,
                    productName = product.productName,
                    price = product.price,
                    quantity = quantity
                )
            )
        } else {
            item.quantity += quantity
        }
    }

    fun total(): Long {
        return items.sumOf { it.price * it.quantity }
    }

    fun clear() {
        customer = null
        items.clear()
    }
}