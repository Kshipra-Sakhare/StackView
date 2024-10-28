package com.example.stackview.stackview.network

interface ApiService {
    @GET("test_mint")
    suspend fun getStackItems(): List<StackItemResponse>
}

annotation class GET(val value: String)

data class StackItemResponse(
    val id: String,
    val title: String,
    val description: String
)