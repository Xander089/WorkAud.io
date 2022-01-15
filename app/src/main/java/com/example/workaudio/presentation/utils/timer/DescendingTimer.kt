package com.example.workaudio.presentation.utils.timer

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class DescendingTimer(
    private val seconds: Int,
    private val delayTime: Long = 1000L
    ) : AbstractTimer() {

    fun setup() {
        this.timer = flow<Int> {
            var currentTime = seconds
            while (currentTime > 0) {
                delay(delayTime)
                currentTime--
                emit(currentTime)
            }
        }
    }

    override fun get() = timer

    init {
        setup()
    }

}