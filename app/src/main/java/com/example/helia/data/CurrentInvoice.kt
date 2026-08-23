package com.example.helia.data

import com.example.helia.model.Customer
import com.example.helia.model.Product
import com.example.helia.model.InvoiceItem

object CurrentInvoice {
    // مشتری انتخاب شده
    var customer: Customer? = null
    // اقلام فاکتور
    val items = mutableListOf<InvoiceItem>()

    fun addItem(product: Product, quantity: Int, returnedQuantity: Int = 0) {
        val item = items.find { it.productID == product.productID }

        if (item == null) {
            items.add(
                InvoiceItem(
                    productID = product.productID,
                    productName = product.productName,
                    price = product.price,
                    quantity = quantity,
                    returnedQuantity = returnedQuantity
                )
            )
        } else {
            item.quantity += quantity
            item.returnedQuantity += returnedQuantity

        }
    }

    fun total(): Long {
        return items.sumOf {
            it.price * (it.quantity - it.returnedQuantity)
        }
    }

    fun clear() {
        customer = null
        items.clear()
    }
}