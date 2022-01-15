package com.example.workaudio

import com.example.workaudio.Constants.RESET_TIME
import com.example.workaudio.core.usecases.player.PlayerInteractor

object DataHelper {


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

}