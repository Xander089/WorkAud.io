package com.example.workaudio.dataAccessImplementation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.core.usecases.detail.DetailDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
import com.example.workaudio.core.usecases.workoutList.ListDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.database.WorkoutRoomEntity
import com.example.workaudio.data.web.SpotifyRestApi
import com.example.workaudio.data.web.SpotifyWebService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        val id = dao.getLatestWorkout().id
        val result = dataAccess.getWorkoutAsFlow(id).first()
        dao.clearWorkouts()
        assertEquals("test_name", result.name)

    }

    @Test
    fun when_name_parameter_is_new_name_then_workout_name_is_updated_accordingly() = runBlocking {
        val id = dao.getLatestWorkout().id
        dataAccess.updateWorkoutName("new_name", id)
        val result = dataAccess.getWorkoutAsFlow(id).first()
        dao.clearWorkouts()
        assertEquals("new_name", result.name)

    }

    @Test
    fun when_duration_parameter_is_ten_then_workout_duration_is_updated_accordingly() = runBlocking {
        val id = dao.getLatestWorkout().id
        dataAccess.updateWorkoutDuration(id,10)
        val result = dataAccess.getWorkoutAsFlow(id).first()
        dao.clearWorkouts()
        assertEquals(10, result.duration)

    }


}