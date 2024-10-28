package com.example.stackview.stackview.model

data class StackItem(
    val id: String,
    val title: String,
    val description: String,
    var state: StackItemState = StackItemState.COLLAPSED
)

enum class StackItemState {
    EXPANDED,
    COLLAPSED
}
