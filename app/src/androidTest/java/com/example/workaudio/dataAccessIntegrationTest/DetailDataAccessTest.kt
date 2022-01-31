package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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

@RunWith(AndroidJUnit4::class)

class DetailDataAccessTest {


    private lateinit var dataAccess: DetailDataAccess
    private lateinit var db: ApplicationDatabase
    private lateinit var dao: ApplicationDAO

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ApplicationDatabase::class.java
        ).build()
        dao = db.applicationDao()
        dataAccess = DetailDataAccess(dao)


        runBlocking {
            val workout =
                WorkoutRoomEntity(name = "test_name", duration = 30 * 1000 * 60, imageUrl = "test")
            dao.clearWorkouts()
            dao.insertWorkout(workout)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun when_workout_is_requested_then_it_is_returned() = runBlocking {
        dao.getLatestWorkout()?.id?.let { id ->
            val result = dataAccess.getWorkoutAsFlow(id).first()
            dao.clearWorkouts()
            assertEquals("test_name", result?.name)
        }
    }

    @Test
    fun when_name_parameter_is_new_name_then_workout_name_is_updated_accordingly() = runBlocking {
        dao.getLatestWorkout()?.id?.let { id ->
            dataAccess.updateWorkoutName("new_name", id)
            val result = dataAccess.getWorkoutAsFlow(id).first()
            dao.clearWorkouts()
            assertEquals("new_name", result?.name)
        }
    }

    @Test
    fun when_duration_parameter_is_ten_then_workout_duration_is_updated_accordingly() = runBlocking {
        dao.getLatestWorkout()?.id?.let { id ->
            dataAccess.updateWorkoutDuration(id,10)
            val result = dataAccess.getWorkoutAsFlow(id).first()
            dao.clearWorkouts()
            assertEquals(10, result?.duration)
        }
    }


}