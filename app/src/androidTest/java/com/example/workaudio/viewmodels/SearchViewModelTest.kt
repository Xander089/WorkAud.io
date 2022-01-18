package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.DataHelper.getOrAwaitValue
import com.example.workaudio.core.entities.Track
import com.example.workaudio.presentation.searchTracks.SearchTracksFragmentViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeSearchBoundary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchTracksFragmentViewModel

    @Before
    fun setup() {
        viewModel = SearchTracksFragmentViewModel(FakeSearchBoundary())
        viewModel.setDispatcher(Dispatchers.Main)
        viewModel.setupCurrentWorkout(0)

    }


    @Test
    fun test_setupCurrentWorkout() = runBlocking {
        //GIVEN
        //WHEN
        val workoutName = viewModel.currentWorkout.getOrAwaitValue().name
        val trackName = viewModel.workoutTracks.getOrAwaitValue()[0].title
        //THEN
        assert(workoutName == "test_name" && trackName == "title")
    }

    @Test
    fun test_addTrack() = runBlocking {
        //GIVEN
        val track = Track("title2", "uri", 1000, "artist", "album", "url", 0)
        //WHEN
        viewModel.addTrack(track, 0)
        //THEN
        val title = viewModel.workoutTracks.getOrAwaitValue()[1].title
        assert(title == "title2")
    }

    @Test
    fun test_searchTracks() {
        //GIVEN

        //WHEN
        viewModel.searchTracks("abc")
        //THEN
        val title = viewModel.searchedTracks.getOrAwaitValue()[0].title
        assert(title == "title")
    }

    @Test
    fun test_updateWorkoutDefaultImage() {
        //GIVEN
        val url = "abc"
        //WHEN
        viewModel.updateWorkoutDefaultImage(url, 0)
        //THEN
        val newUrl = viewModel.currentWorkout.getOrAwaitValue().imageUrl
        assert(newUrl == url)
    }

    @Test
    fun test_updateProgressBar() = runBlocking {
        //GIVEN
        val workout = viewModel.currentWorkout.getOrAwaitValue()
        viewModel.setTargetDuration(workout)
        val tracks = viewModel.workoutTracks.getOrAwaitValue()
        //WHEN
        val progress = viewModel.updateProgressBar(tracks)
        //THEN
        assert(progress == 10)
    }

    @Test
    fun test_formatCurrentDuration() {
        //GIVEN
        val tracks = viewModel.workoutTracks.getOrAwaitValue()
        //WHEN
        val duration = viewModel.formatCurrentDuration(tracks)
        //THEN
        assert(duration == "3")
    }

    @Test
    fun test_formatDuration() {
        //GIVEN

        //WHEN
        val formatted = viewModel.formatDuration(1000*60*3)
        //THEN
        assert(formatted == "/3 min")
    }

    @Test
    fun test_formatSnackBarText() {
        //GIVEN

        //WHEN
        val res = viewModel.formatSnackBarText("title", "decoration")
        //THEN
        assert(res == "title decoration")
    }


}