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
import com.example.helia.model.ServerShamsiDateResponse
import okhttp3.internal.concurrent.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginRequest(
    val username: String,
    val password: String
)

interface ApiService {
    @POST("api/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

    @GET("api/Customers")
    suspend fun getCustomers(): ApiResponse<List<Customer>>

    //    @GET("api/products")
//    suspend fun getProducts(): ApiResponse<List<Product>>
    @GET("api/products")
    suspend fun getProducts(
        @Query("customerID") customerID: String
    ): ApiResponse<List<Product>>


    @POST("api/products")
    suspend fun addItem(
        @Body request: InvoiceDetailRequest
    ): Response<ApiResponse<Boolean>>

    @POST("api/Invoices")
    suspend fun saveInvoice(
        @Body request: InvoiceRequest
//    ): ApiResponse<Long>
    ): ApiResponse<String>

    @GET("api/Invoices/customer/{customerID}")
//    suspend fun getInvoices(
    suspend fun GetCustomerInvoices(
        @Path("customerID")
//        customerID: Int
        customerID: String
    ): ApiResponse<List<InvoiceHistory>>

    @GET("api/Invoices/{invoiceID}")
    suspend fun getInvoice(
        @Path("invoiceID")
//        invoiceID: Long
        invoiceID: String
    ): ApiResponse<InvoiceDetailResponse>

    @DELETE("api/Invoices/{invoiceID}")
    suspend fun deleteInvoice(
//        @Path("invoiceID") invoiceID: Long
        @Path("invoiceID") invoiceID: String
    ): ApiResponse<Boolean>

    @PUT("api/Invoices/{invoiceID}")
    suspend fun updateInvoice(
        @Path("invoiceID")
//        invoiceID: Long,
        invoiceID: String,
        @Body
        request: InvoiceRequest
    ): ApiResponse<String>

    @POST("api/invoices/{invoiceID}/items")
    suspend fun addItem(
//        @Path("invoiceID") invoiceID: Int,
        @Path("invoiceID") invoiceID: String,
        @Body request: InvoiceDetailRequest
    ): Response<ApiResponse<Boolean>>

    @DELETE("api/invoices/{invoiceID}/items/{detailID}")
    suspend fun deleteItem(
//        @Path("invoiceID") invoiceID: Int,
        @Path("invoiceID") invoiceID: String,
        @Path("detailID") detailID: Int
    ): Response<ApiResponse<Boolean>>

    @GET("api/system/date")
    suspend fun getServerShamsiDate(): Response<ServerShamsiDateResponse>

}
