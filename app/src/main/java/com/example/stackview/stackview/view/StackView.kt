package com.example.stackview.stackview.view

import android.content.Context
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.stackview.stackview.model.StackItem

class StackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private var items: List<StackItem> = emptyList()
    private val cardViews = mutableListOf<CardView>()
    private val expandAnimDuration = 300L

    init {
        orientation = VERTICAL
        clipChildren = false
        clipToPadding = false
    }
}