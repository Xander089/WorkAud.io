package com.example.workaudio.presentation.utils

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onRowMoved(fromPosition: Int, toPosition: Int)
}