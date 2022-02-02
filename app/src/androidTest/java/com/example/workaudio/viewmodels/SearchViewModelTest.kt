package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.workaudio.common.DataHelper.getOrAwaitValue
import com.example.workaudio.TestDataSource
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.searchTracks.SearchBoundary
import com.example.workaudio.presentation.searchTracks.SearchTracksFragmentViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var source : TestDataSource

    private lateinit var viewModel: SearchTracksFragmentViewModel

    @Mock
    lateinit var interactor: SearchBoundary

    @Before
    fun setup() {
        source = TestDataSource()
        setupMock()
        hiltRule.inject()
        viewModel = SearchTracksFragmentViewModel(interactor)
        viewModel.setDispatcher(Dispatchers.Main)
        viewModel.setupCurrentWorkout(0)

    }

    private fun setupMock(){
        `when`(interactor.getWorkout(0)).thenReturn(flow { emit(source.workout) })
        `when`(interactor.getWorkoutTracks(0)).thenReturn(flow { source.tracks })
    }


    @Test
    fun whenWorkoutIsSet_thenItIsReturnedAsLiveData() = runBlocking(Dispatchers.Main) {
        //GIVEN
        val expected = "test_name"
        //WHEN
        val workoutName = viewModel.currentWorkout.getOrAwaitValue()?.name
        //THEN
        assertEquals(expected,workoutName)
    }

    @Test
    fun whenTrackIsAdded_thenTheListContainsIt() = runBlocking(Dispatchers.Main) {
        //GIVEN
        val track = source.tracks[1]
        val expected = track.title
        `when`(interactor.addTrack(track,0)).thenReturn(addTrack(track))
        //WHEN
        viewModel.addTrack(track, 0)
        //THEN
        val actual = source.tracks[2].title
        assertEquals(expected, actual)
    }

    private fun addTrack(track: Track){
        source.tracks.add(track)
    }

    @Test
    fun whenTracksAreSearched_thenTheyAreReturned() = runBlocking(Dispatchers.Main)  {
        //GIVEN
        val expected = "title1"
        val query = "query"
        //WHEN
        `when`(interactor.searchTracks(query)).thenReturn(source.tracks)
        viewModel.searchTracks(query)
        delay(1000)
        //THEN
        val actual = viewModel.searchedTracks.getOrAwaitValue()[0].title
        assertEquals(expected,actual)

    }

    @Test
    fun whenDefaultImageUrlUpdated_thenWorkoutContainsIt() = runBlocking {
        //GIVEN
        val expected = "newImageUrl"
        `when`(interactor.updateWorkoutDefaultImage(expected,0)).thenReturn(updateUrl(expected))
        //WHEN
        viewModel.updateWorkoutDefaultImage(expected, 0)
        //THEN
        val actual = source.workout.imageUrl
        assertEquals(expected,actual)

    }

    private fun updateUrl(url: String){
        source.workout.imageUrl = url
    }

    @Test
    fun whenTotalTimeIsThree_thenThreeIsReturnedAsDurationString() {
        //GIVEN
        val expected = "3"
        //WHEN
        val actual = viewModel.formatCurrentDuration(source.tracks)
        //THEN
        assertEquals(expected,actual)
    }

    @Test
    fun whenInputIs180Seconds_then_3MinIsReturnedAsString() {
        //GIVEN
        val expected = "/3 min"
        //WHEN
        val actual = viewModel.formatDuration(1000 * 60 * 3)
        //THEN
        assertEquals(expected,actual)
    }

    @Test
    fun test_formatSnackBarText() {
        //GIVEN
        val expected = "title decoration"
        //WHEN
        val actual = viewModel.formatSnackBarText("title", "decoration")
        //THEN
        assertEquals(expected,actual)
    }


}