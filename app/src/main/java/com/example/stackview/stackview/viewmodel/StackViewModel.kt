package com.example.stackview.stackview.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stackview.stackview.model.StackItem
import com.example.stackview.stackview.model.StackItemState
import com.example.stackview.stackview.network.RetrofitInstance
import kotlinx.coroutines.launch

class StackViewModel : ViewModel() {

    private val _stackItems = MutableLiveData<List<StackItem>>()
    val stackItems: LiveData<List<StackItem>> = _stackItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()  // Nullable to handle no error case
    val error: LiveData<String?> = _error

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("StackViewModel", "Fetching items...")
                val response = RetrofitInstance.api.getStackItems()  // Gets ApiResponse
                Log.d("StackViewModel", "Response received: $response")

                // Map ApiResponse items to StackItem model
                _stackItems.value = response.items.map { item ->
                    StackItem(
                        id = item.open_state.body.title, // Assuming title as unique ID
                        title = item.open_state.body.title,
                        description = item.open_state.body.subtitle,
                        footer = item.open_state.body.footer,
                        ctaText = item.cta_text,
                        state = StackItemState.COLLAPSED // Initial state
                    )
                }
            } catch (e: Exception) {
                Log.e("StackViewModel", "Error fetching items: $e")
                val errorMessage = when (e) {
                    is java.net.SocketTimeoutException -> "Connection timed out. Please check your internet connection and try again."
                    is java.net.UnknownHostException -> "Unable to reach server. Please check your internet connection."
                    else -> "An error occurred: ${e.message ?: "Unknown error"}"
                }
                _error.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Toggle item state (expanded/collapsed)
    fun toggleItemState(itemId: String) {
        _stackItems.value = _stackItems.value?.map { item ->
            if (item.id == itemId) {
                val newState = if (item.state == StackItemState.EXPANDED) StackItemState.COLLAPSED else StackItemState.EXPANDED
                item.copy(state = newState)
            } else {
                item
            }
        }
    }

    // Retry network call
    fun retry() {
        _error.value = null // Clear error before retry
        fetchItems()
    }
}
