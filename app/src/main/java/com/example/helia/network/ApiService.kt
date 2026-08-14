package com.example.helia.network

import android.R.bool
import com.example.helia.model.ApiResponse
import com.example.helia.model.Customer
import com.example.helia.model.InvoiceDetailRequest
import com.example.helia.model.InvoiceDetailResponse
import com.example.helia.model.InvoiceHistory
import com.example.helia.model.InvoiceRequest
import com.example.helia.model.LoginResponse
import com.example.helia.model.Product
import okhttp3.internal.concurrent.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(
    val username:String,
    val password:String
)
interface ApiService {
    @POST("api/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>
    @GET("api/Customers")
    suspend fun getCustomers(): ApiResponse<List<Customer>>

//    @GET("api/products")
//    suspend fun getProducts(): Response<List<Product>>

    @GET("api/products")
    suspend fun getProducts(): ApiResponse<List<Product>>

    @POST("api/products")
    suspend fun addItem(
        @Body request: InvoiceDetailRequest
    ): Response<ApiResponse<Boolean>>

    @POST("api/Invoices")
    suspend fun saveInvoice(
        @Body request: InvoiceRequest
    ): ApiResponse<Long>

    @GET("api/Invoices/customer/{customerID}")
    suspend fun getInvoices(
        @Path("customerID")
        customerID: Int
    ): ApiResponse<List<InvoiceHistory>>

    @GET("api/Invoices/{invoiceID}")
    suspend fun getInvoice(
        @Path("invoiceID")
        invoiceID: Long
    ): ApiResponse<InvoiceDetailResponse>

    @DELETE("api/Invoices/{invoiceID}")
    suspend fun deleteInvoice(
        @Path("invoiceID") invoiceID: Long
    ): ApiResponse<Boolean>

    @PUT("api/Invoices/{invoiceID}")
    suspend fun updateInvoice(
        @Path("invoiceID")
        invoiceID: Long,
        @Body
        request: InvoiceRequest
    ): ApiResponse<String>

    @POST("api/invoices/{invoiceID}/items")
    suspend fun addItem(
        @Path("invoiceID") invoiceID: Int,
        @Body request: InvoiceDetailRequest
    ): Response<ApiResponse<Boolean>>

//    @DELETE("api/Invoices/items/{detailID}")
//    suspend fun deleteItem(
//        @Path("detailID") detailID: Long
//    ): ApiResponse<Boolean>

    @DELETE("api/invoices/{invoiceID}/items/{detailID}")
    suspend fun deleteItem(
        @Path("invoiceID") invoiceID: Int,
        @Path("detailID") detailID: Int
    ): Response<ApiResponse<Boolean>>

}
