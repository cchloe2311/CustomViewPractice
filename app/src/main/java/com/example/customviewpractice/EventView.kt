package com.example.customviewpractice

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.event_view.view.*

class EventView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.event_view, this)
    }

    fun setData(event: Event) {
        iv_description.setImageResource(event.descriptionImage)
        tv_title.text = event.title
        tv_year.text = event.year.toString()
        tv_description.text = event.descriptionText
    }
}