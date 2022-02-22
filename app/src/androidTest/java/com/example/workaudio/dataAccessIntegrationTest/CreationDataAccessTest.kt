package com.example.workaudio.dataAccessIntegrationTest

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.workaudio.TestDataSource
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.TestRetrofitFactory
import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@MediumTest
@RunWith(AndroidJUnit4::class)
class CreationDataAccessTest {


    private lateinit var dataAccess: CreationDataAccess
    private lateinit var source: TestDataSource
    private lateinit var dao: ApplicationDAO

    @Before
    fun setup() {
        source = TestDataSource()
        dao = TestDatabaseFactory.createDao()!!

        dataAccess = CreationDataAccess(dao)

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
    fun whenNewWorkoutIsCreated_thenItIsReturned() =
        runBlocking {
            //Given
            val expected = "new_name"
            //When
            dataAccess.insertWorkout("new_name", 0)
            val workoutName = dao.getLatestWorkoutAsFlow().first()?.name.orEmpty()
            dao.clearWorkouts()
            //Then
            assertEquals(expected, workoutName)
        }

    @Test
    fun whenNewWorkoutIsNotCreated_thenItIsNotReturned() =
        runBlocking {
            //Given
            //When
            dao.clearWorkouts()
            val workoutName = dao.getLatestWorkoutAsFlow().first()?.name.orEmpty()
            //Then
            assertTrue(workoutName.isEmpty())
        }
}