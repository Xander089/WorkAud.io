package com.example.workaudio.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.workaudio.common.Constants.RESET_TIME
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object DataHelper {

    fun durationToMinutes(duration: Int) = "${(duration / Constants.MILLIS_IN_A_MINUTE)}"

    fun fromTimeToSeconds(time: String): Int {
        val timeList = time.split(":").map {
            it.toInt()
        }
        return timeList[0] * 3600 + timeList[1] * 60 + timeList[2]
    }

    fun fromMinutesToSeconds(time: String): Int {
        val timeList = time.split(":").map {
            it.toInt()
        }
        return timeList[0] * 60 + timeList[1]
    }

    fun formatDuration(duration: Float): String {
        val intDuration = duration.toInt()
        return "$intDuration${Constants.MIN}"
    }

    fun formatTrackDuration(seconds: Int): String {
        val minutes = seconds / 60
        val remainder = seconds % 60
        val r = if(remainder < 10) "0$remainder" else "$remainder"
        return "$minutes:$r"
    }

    fun toTime(seconds: Int): String {

        if (seconds <= 0) {
            return RESET_TIME
        }

        val hour = seconds / 3600
        var remainderSeconds = seconds % 3600
        val minutes = remainderSeconds / 60
        remainderSeconds %= 60

        val h = if (hour < 10) "0$hour" else "$hour"
        val m = if (minutes < 10) "0$minutes" else "$minutes"
        val s = if (remainderSeconds < 10) "0$remainderSeconds" else "$remainderSeconds"

        return "$h:$m:$s"
    }

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

}