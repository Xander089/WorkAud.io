package com.example.workaudio.dataAccessIntegrationTest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDataSource
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.TestRetrofitFactory
import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CreationDataAccessTest {


    private lateinit var dataAccess: CreationDataAccess
    private lateinit var source: TestDataSource
    private lateinit var dao: ApplicationDAO

    @Before
    fun setup() {
        source = TestDataSource()
        val webService = TestRetrofitFactory.createWebService()
        dao = TestDatabaseFactory.createDao()!!

        dataAccess = CreationDataAccess(
            dao,
            webService
        )

        runBlocking {
            dao.clearWorkouts()
            dao.insertWorkout(source.workoutRoomEntity)
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        TestDatabaseFactory.disposeDb()
    }


    @Test
    fun whenLatestWorkoutIsRequested_thenItIsReturned() = runBlocking {
        //Given
        val expected = "test_name"
        //When
        val workoutName = dataAccess.getWorkout()?.name.orEmpty()
        dao.clearWorkouts()
        //Then
        assertEquals(expected, workoutName)
    }

    @Test
    fun whenLatestWorkoutIsRequestedAsFlow_thenItIsReturned() = runBlocking {
        //Given
        val expected = "test_name"
        //When
        val workoutName = dataAccess.getLatestWorkoutAsFlow().first()?.name.orEmpty()
        dao.clearWorkouts()
        //Then
        assertEquals(expected, workoutName)

    }

    @Test
    fun whenNewWorkoutIsCreated_thenItIsReturned() =
        runBlocking {
            //Given
            val expected = "new_name"
            //When
            dataAccess.insertWorkout("new_name", 0)
            val workoutName = dataAccess.getLatestWorkoutAsFlow().first()?.name.orEmpty()
            dao.clearWorkouts()
            //Then
            assertEquals(expected, workoutName)
        }
}