package com.example.workaudio.presentation.utils.itemTouchHelper

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onRowMoved(fromPosition: Int, toPosition: Int)
}