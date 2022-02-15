package com.example.workaudio.presentation.utils.adapter

import android.widget.ImageView
import com.example.workaudio.core.entities.Track

object AdapterFactory {


    fun create(
        flavour: AdapterFlavour,
        fetchImage: (ImageView, String) -> Unit = { _, _ -> },
        addTrack: (Track) -> Unit = {},
        deleteTrack: (String) -> Unit = {},
        open: (String) -> Unit = {},
        onSwipe: (String) -> Unit = {},
        openWorkoutDetail: (Int) -> Unit = {},
        showBottomModal: (Int) -> Unit = {},
    ): BaseAdapter {
        return when (flavour) {
            AdapterFlavour.PLAYER -> PlayerTracksAdapter(mutableListOf(), fetchImage)
            AdapterFlavour.SEARCHED -> SearchedTracksAdapter(mutableListOf(), addTrack, fetchImage)
            AdapterFlavour.DETAIL -> DetailTracksAdapter(
                mutableListOf(),
                fetchImage,
                deleteTrack,
                onSwipe
            )
            AdapterFlavour.WORKOUT -> WorkoutAdapter(
                mutableListOf(),
                openWorkoutDetail,
                showBottomModal,
                fetchImage
            )
            AdapterFlavour.GENRE -> GenreAdapter(mutableListOf(), open)
            else -> PlayerTracksAdapter(mutableListOf(), fetchImage)

        }
    }
}