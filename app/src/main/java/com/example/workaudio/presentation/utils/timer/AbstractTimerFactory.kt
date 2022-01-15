package com.example.workaudio.presentation.utils.timer


interface AbstractTimerFactory {

    fun create(
        seconds: Int,
        delayTime: Long = 1000L,
        ascending: Boolean
    ): AbstractTimer

}