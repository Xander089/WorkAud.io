package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.workaudio.TestDataSource
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.core.usecases.detail.DetailDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.database.WorkoutRoomEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.math.exp

@MediumTest
@RunWith(AndroidJUnit4::class)
class DetailDataAccessTest {


    private lateinit var dataAccess: DetailDataAccess
    private lateinit var dao: ApplicationDAO
    private lateinit var source: TestDataSource
    private var id = 0

    @Before
    fun createDb() {
        dao = TestDatabaseFactory.createDao()!!
        dataAccess = DetailDataAccess(dao)
        source = TestDataSource()

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
    fun whenWorkoutRequestedAsFlow_thenItIsEmitted() = runBlocking {
        //Given
        val expected = "test_name"
        //When
        val actual = dataAccess.getWorkoutAsFlow(id).first()?.name.orEmpty()
        //Then
        assertEquals(expected, actual)
        dao.clearWorkouts()
    }

    @Test
    fun whenNameParameterIs_newName_thenWorkoutNameUpdatedAccordingly() = runBlocking {
        //Given
        val expected = "new_name"
        //When
        dataAccess.updateWorkoutName(expected, id)
        val actual = dataAccess.getWorkoutAsFlow(id).first()?.name.orEmpty()
        //Then
        assertEquals(expected, actual)
        dao.clearWorkouts()
    }

    @Test
    fun whenDurationParameterIs_10_thenWorkoutDurationUpdatedAccordingly() =
        runBlocking {
            //Given
            val expected = 10
            //When
            dataAccess.updateWorkoutDuration(id, expected)
            val actual = dataAccess.getWorkoutAsFlow(id).first()?.duration?.or(0)
            //Then
            assertEquals(expected, actual)
            dao.clearWorkouts()
        }


}