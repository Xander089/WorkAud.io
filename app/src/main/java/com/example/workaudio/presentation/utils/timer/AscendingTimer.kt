package com.example.workaudio.presentation.utils.timer

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class AscendingTimer(
    private val seconds: Int,
    private val delayTime: Long = 1000L
) : AbstractTimer() {

    fun setup() {
        this.timer = flow<Int> {
            var currentTime = 0
            while (currentTime <= seconds) {
                delay(delayTime)
                currentTime++
                emit(currentTime)
            }
        }
    }

    override fun get() = timer

    init {
        setup()
    }

}