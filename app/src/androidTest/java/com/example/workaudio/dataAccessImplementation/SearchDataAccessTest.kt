package com.example.workaudio.dataAccessImplementation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
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

class SearchDataAccessTest {

    companion object {
        private const val ENDPOINT = "https://api.spotify.com/v1/"
    }

    private lateinit var dataAccess: SearchDataAccess
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
        dataAccess = SearchDataAccess(dao, webService)


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
    fun test_searchTracks() = runBlocking {
        val queryText = "Radio Gaga"
        val title = dataAccess.searchTracks(queryText)[0].title
        val result = title.contains("Radio") || title.contains("Gaga")
        assert(title.isNotEmpty())
    }

    @Test
    fun test_insertWorkout_then_getWorkoutTracksAsFlow() = runBlocking {
        val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
        dataAccess.insertTrack(track, dao.getLatestWorkout().id)
        val resultTrack = dataAccess.getWorkoutTracksAsFlow(dao.getLatestWorkout().id).first()[0]
        dao.clearWorkoutsTracks()
        assert(track.title == resultTrack.title)

    }

    @Test
    fun test_updateImageUrl_then_getWorkoutAsFlow_and_check_url() = runBlocking {
        dataAccess.updateWorkoutDefaultImage("new",dao.getLatestWorkout().id)
        val newUrl = dataAccess.getWorkoutAsFlow(dao.getLatestWorkout().id).first().imageUrl
        dao.clearWorkouts()
        assert(newUrl == "new")
    }

}