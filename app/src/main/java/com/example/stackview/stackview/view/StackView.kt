package com.example.stackview.stackview.view

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.stackview.StackActivity
import com.example.stackview.stackview.model.StackItem
import com.example.stackview.stackview.model.StackItemState

class StackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var items: List<StackItem> = emptyList() // Holds the list of items
    private val cardViews = mutableListOf<CardView>() // Holds the created CardViews

    init {
        orientation = VERTICAL // Set the orientation to vertical
    }

    // Method to set and render items
    fun setItems(newItems: List<StackItem>) {
        removeAllViews() // Clear existing views
        items = newItems // Update the items list
        items.forEach { item ->
            val cardView = createCardView(item) // Create a CardView for each item
            addView(cardView) // Add the CardView to this StackView
            cardViews.add(cardView) // Keep track of the CardViews
        }
    }

    // Helper method to create each CardView
    private fun createCardView(item: StackItem): CardView {
        val cardView = CardView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 8, 8) // Set margins for the CardView
            }
            radius = 8f // Set the corner radius
            elevation = 4f // Set elevation for shadow effect
        }

        // Inflate and set content for the CardView
        val content = inflateCardContent(item)
        cardView.addView(content)

        // Set up click listener to toggle item state
        cardView.setOnClickListener {
            (context as? StackActivity)?.onCardClicked(item.id) // Call toggle function
        }
        return cardView
    }

    // Inflate the card content based on the StackItem
    private fun inflateCardContent(item: StackItem): LinearLayout {
        val layout = LinearLayout(context).apply {
            orientation = VERTICAL // Set orientation to vertical
            setPadding(16, 16, 16, 16) // Set padding for the layout
        }

        // Title TextView
        val titleTextView = TextView(context).apply {
            text = item.title // Set title text
            textSize = 18f // Set text size
            setTypeface(typeface, Typeface.BOLD) // Make text bold
        }
        layout.addView(titleTextView) // Add title TextView to the layout

        // Description TextView
        val descriptionTextView = TextView(context).apply {
            text = item.description // Set description text
            textSize = 14f // Set text size
            visibility = if (item.state == StackItemState.EXPANDED) View.VISIBLE else View.GONE // Show/hide based on state
        }
        layout.addView(descriptionTextView) // Add description TextView to the layout

        return layout // Return the inflated layout
    }
}

