package com.example.workaudio.presentation.utils

import androidx.recyclerview.widget.RecyclerView

object OnScrollListenerFactory {

    fun create(
        onScrollStateChanged: (Int) -> Unit = {},
        onScrolled: (Int) -> Unit = {}
    ) = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }

    }
}