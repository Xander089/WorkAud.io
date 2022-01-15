package com.example.workaudio.presentation.utils.timer

import kotlinx.coroutines.flow.Flow

abstract class AbstractTimer() {
    open lateinit var timer: Flow<Int>
    abstract fun get(): Flow<Int>
}