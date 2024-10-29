package com.example.stackview.stackview.network

import retrofit2.http.GET

interface ApiService {
    @GET("test_mint")
    suspend fun getStackItems(): ApiResponse
}
// Define data model classes that match the JSON structure

data class ApiResponse(
    val items: List<StackItemResponse>
)

data class StackItemResponse(
    val open_state: OpenState,
    val closed_state: ClosedState,
    val cta_text: String
)

data class OpenState(
    val body: Body
)

data class Body(
    val title: String,
    val subtitle: String,
    val card: Card? = null, // Nullable because some items don't have "card"
    val items: List<Item>? = null, // Nullable because not all items have nested "items"
    val footer: String
)

data class Card(
    val header: String,
    val description: String,
    val max_range: Int,
    val min_range: Int
)

data class Item(
    val emi: String? = null, // Nullable because some items don’t have "emi"
    val duration: String? = null,
    val title: String,
    val subtitle: String,
    val tag: String? = null // Nullable since "tag" is optional
)

data class ClosedState(
    val body: Map<String, String> // This can be a Map since key-value pairs vary
)
