package com.example.workaudio.common

import com.example.workaudio.R

object Constants {

    const val LABEL = "Delete Selected Track"
    const val MILLIS_TO_SECOND = 0.001
    const val DEFAULT_DELAY_TIME = 1000L
    const val BEARER = "Bearer"
    const val SEARCH_TYPE = "track"
    const val MODAL_TITLE = "title"
    const val MODAL_ACTION = "action"
    const val TAG = "ModalBottomSheet"
    const val ID_TAG = "id"
    const val WORKOUT_NAME = "workout_name"
    const val WORKOUT_DURATION = "workout_duration"
    const val DEFAULT_DURATION = 30.0f
    const val MIN = " min"
    const val RESET_TIME = "00:00:00"
    const val MILLIS_IN_A_MINUTE = 60000
    const val MILLISECONDS_IN_A_MINUTE = 60000
    const val REQUEST_CODE = 1337
    const val WORKOUT_ID = "WORKOUT_ID"
    const val NAME_TAG = "EditNameDialog"
    const val DURATION_TAG = "EditDurationDialog"
    const val STOP_TAG = "StopPlayerDialog"

    val GENRES = listOf(
        "PUNK",
        "POP",
        "ALTERNATIVE",
        "ROCK",
        "METAL",
        "EDM",
        "HOUSE",
    )

    val colorMap = mapOf(
        0 to R.color.red,
        1 to R.color.emerald,
        2 to R.color.amber,
        3 to R.color.teal,
        4 to R.color.green,
        5 to R.color.orange,
        6 to R.color.pink,
    )


}