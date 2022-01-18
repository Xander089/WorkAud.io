package com.example.workaudio.presentation.utils

import android.widget.SearchView

object QueryTextListenerFactory {

    fun create(action: (String) -> Unit) = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            action(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String) = true
    }

}