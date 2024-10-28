package com.example.stackview.stackview.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.util.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stackview.stackview.model.StackItem
import com.example.stackview.stackview.model.StackItemState
import com.example.stackview.stackview.network.RetrofitInstance
import kotlinx.coroutines.launch

class StackViewModel : ViewModel(){
    private val _stackItems = MutableLiveData<List<StackItem>>()
    val stackItems: LiveData<List<StackItem>> = _stackItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchItems()
    }

    fun fetchItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getStackItems()
                _stackItems.value = response.map {
                    StackItem(it.id, it.title, it.description)
                }
            } catch (e: Exception) {
                _stackItems.value = emptyList()
                // You might want to add error handling here
                Log.e("StackViewModel", "Error fetching items", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleItemState(itemId: String) {
        val currentItems = _stackItems.value?.toMutableList() ?: return

        val expandedIndex = currentItems.indexOfFirst { it.state == StackItemState.EXPANDED }
        if (expandedIndex != -1) {
            currentItems[expandedIndex] = currentItems[expandedIndex].copy(state = StackItemState.COLLAPSED)
        }

        val clickedIndex = currentItems.indexOfFirst { it.id == itemId }
        if (clickedIndex != -1) {
            currentItems[clickedIndex] = currentItems[clickedIndex].copy(state = StackItemState.EXPANDED)
        }

        _stackItems.value = currentItems
    }
}