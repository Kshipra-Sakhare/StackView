package com.example.stackview.stackview.model

data class StackItem(
    val id: String,
    val title: String,
    val description: String,
    val footer: String? = null,  // Nullable to handle missing values
    val ctaText: String? = null, // Nullable to handle missing values
    val state: StackItemState
)

enum class StackItemState {
    EXPANDED,
    COLLAPSED
}
