package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDataSource
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.workoutList.ListDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.database.WorkoutRoomEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class MainListDataAccessTest {


    private lateinit var dataAccess: ListDataAccess
    private lateinit var dao: ApplicationDAO
    private lateinit var source: TestDataSource
    private var id = 0


    @Before
    fun createDb() {
        source = TestDataSource()
        dao = TestDatabaseFactory.createDao()!!
        dataAccess = ListDataAccess(dao)

        runBlocking {
            dao.clearWorkouts()
            dao.insertWorkout(source.workoutRoomEntity)
            id = dao.getLatestWorkout()?.id ?: 0

        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        TestDatabaseFactory.disposeDb()
    }


    @Test
    fun whenAllWorkoutsRequested_then_theyAreReturned() = runBlocking {
        //Given
        val expected = "test_name"
        //When
        val actual = dataAccess.getWorkouts().first()?.get(0)?.name
        //Then
        assertEquals(expected, actual)
        dao.clearWorkouts()

    }

    @Test
    fun whenWorkout_isDeleted_then_dbDoesNotContainItAnyMore() = runBlocking {
        //When
        dataAccess.deleteWorkout(id)
        //Then
        val workout = dao.getWorkout(id)
        assertTrue(workout == null)
    }

    @Test
    fun whenTrackRequestedAndIsCached_thenItIsReturned() = runBlocking {
        //When
        dao.insertWorkoutTrack(source.workoutTracksRoomEntity)
        val actual = dataAccess.getWorkoutTrack(id)?.title.orEmpty()
        //Then
        assertEquals("test", actual)

        dao.clearWorkouts()
        dao.clearWorkoutsTracks()

    }


}