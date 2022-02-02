package com.example.workaudio.presentation.utils.timer

class TimerFactoryImpl : AbstractTimerFactory {

    companion object {
        fun newInstance() = TimerFactoryImpl()
    }

    override fun create(
        seconds: Int,
        delayTime: Long,
        ascending: Boolean
    ): AbstractTimer =
        if (ascending) {
            AscendingTimer(seconds, delayTime)
        } else {
            DescendingTimer(seconds, delayTime)
        }

}