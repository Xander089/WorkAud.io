package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDataSource
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.core.usecases.player.PlayerDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.database.WorkoutRoomEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class PlayerDataAccessTest {


    private lateinit var dataAccess: PlayerDataAccess
    private lateinit var dao: ApplicationDAO
    private lateinit var source: TestDataSource
    private var id = 0


    @Before
    fun createDb() {
        source = TestDataSource()
        dao = TestDatabaseFactory.createDao()!!
        dataAccess = PlayerDataAccess(dao)

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
    fun whenWorkoutRequested_thenItIsReturned() = runBlocking {
        val expected = "test_name"
        //When
        val result = dataAccess.getWorkout(id)
        //Then
        assertEquals(expected, result?.name)
        dao.clearWorkouts()

    }

}