package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class MainListDataAccessTest {


    private lateinit var dataAccess: ListDataAccess
    private lateinit var db: ApplicationDatabase
    private lateinit var dao: ApplicationDAO

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ApplicationDatabase::class.java
        ).build()
        dao = db.applicationDao()
        dataAccess = ListDataAccess(dao)


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
    fun when_all_workout_are_requested_then_they_are_returned() = runBlocking {
        val workoutName = dataAccess.getWorkouts().first()?.get(0)?.name
        dao.clearWorkouts()
        assertEquals(workoutName, "test_name")

    }

    @Test
    fun when_a_workout_is_deleted_then_null_is_returned() = runBlocking {
        dao.getLatestWorkout()?.id?.let { id ->
            dataAccess.deleteWorkout(id)
            val workout = dao.getWorkout(id)
            assert(workout == null)
        }
    }

    @Test
    fun when_a_workout_track_is_requested_then_it_is_returned() = runBlocking {
        dao.getLatestWorkout()?.id?.let { id ->
            val track = Track("title", "uri", 1000, "artist", "album", "url", 0).toTrackRoomEntity(id)
            dao.insertWorkoutTrack(track)
            val result = dataAccess.getWorkoutTrack(id)?.title.orEmpty()
            dao.clearWorkouts()
            dao.clearWorkoutsTracks()
            assertEquals("title", result)
        }
    }


}