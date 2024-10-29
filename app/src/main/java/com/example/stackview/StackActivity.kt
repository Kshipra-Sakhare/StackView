package com.example.stackview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stackview.stackview.view.StackView
import com.example.stackview.stackview.viewmodel.StackViewModel

class StackActivity : AppCompatActivity() {

    private val stackViewModel: StackViewModel by viewModels() // Using ViewModel delegate
    private lateinit var stackView: StackView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stack)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack) // Layout for this activity

        stackView = findViewById(R.id.stackView)

        // Observe changes to stackItems
        stackViewModel.stackItems.observe(this) { items ->
            stackView.setItems(items) // Update StackView with new items
        }

        // Observe loading state
        stackViewModel.isLoading.observe(this) { isLoading ->
            // Handle loading state (e.g., show/hide a loading spinner)
        }

        // Observe error state
        stackViewModel.error.observe(this) { error ->
            // Handle error state (e.g., show a message)
        }
    }

    fun onCardClicked(itemId: String) {
        stackViewModel.toggleItemState(itemId) // Toggle state for clicked item
    }
}