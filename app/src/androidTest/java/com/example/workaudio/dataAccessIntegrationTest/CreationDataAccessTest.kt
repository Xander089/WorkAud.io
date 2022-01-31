package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.database.WorkoutRoomEntity
import com.example.workaudio.data.web.SpotifyRestApi
import com.example.workaudio.data.web.SpotifyWebService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class CreationDataAccessTest {

    companion object {
        private const val ENDPOINT = "https://api.spotify.com/v1/"
    }

    private lateinit var dataAccess: CreationDataAccess
    private lateinit var db: ApplicationDatabase
    private lateinit var dao: ApplicationDAO

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ApplicationDatabase::class.java
        ).build()
        dao = db.applicationDao()
        val retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(SpotifyRestApi::class.java)
        val webService = SpotifyWebService(api)
        dataAccess = CreationDataAccess(dao, webService)


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
    fun when_latest_workout_is_requested_then_it_is_returned() = runBlocking {
        val workoutName = dataAccess.getWorkout()?.name.orEmpty()
        dao.clearWorkouts()
        assert(workoutName == "test_name")

    }

    @Test
    fun when_latest_workout_is_requested_as_flow_then_it_is_returned() = runBlocking {
        val workoutName = dataAccess.getLatestWorkoutAsFlow().first()?.name.orEmpty()
        dao.clearWorkouts()
        assert(workoutName == "test_name")
    }

    @Test
    fun when_a_new_workout_is_created_then_it_is_returned_as_flow_as_latest_workout() = runBlocking {
        dataAccess.insertWorkout("new_name",0)
        val workoutName = dataAccess.getLatestWorkoutAsFlow().first()?.name.orEmpty()
        dao.clearWorkouts()
        assert(workoutName == "new_name")
    }

}