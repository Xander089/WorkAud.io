package com.example.workaudio

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchInteractorTest {

    private lateinit var searchInteractor: SearchInteractor

    @Before
    fun setUp() {
        searchInteractor = SearchInteractor(FakeSearchDataAccess())
    }


    @Test
    fun getWorkout() = runBlocking {
        searchInteractor.getWorkout(0).first().let {
            assert(it.name == "test_name")
        }
    }

    @Test
    fun getWorkoutTracks() = runBlocking {
        searchInteractor.getWorkoutTracks(0).first()[0].let {
            assert(it.title == "title")
        }
    }

    @Test
    fun searchTracks() = runBlocking {
        searchInteractor.searchTracks("")[0].let {
            assert(it.title == "title")
        }
    }

    @Test
    fun addTrack() = runBlocking {
        val track = Track("title2", "uri2", 1000, "artist", "album", "url", 0)
        searchInteractor.addTrack(track, 0)
        searchInteractor.getWorkoutTracks(0).first()[1].let {
            assert(it.title == "title2")
        }
    }

    @Test
    fun updateWorkoutDefaultImage() = runBlocking {
        searchInteractor.updateWorkoutDefaultImage("new", 0)
        searchInteractor.getWorkout(0).first().let {
            assert(it.imageUrl == "new")
        }
    }
}