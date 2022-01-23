package com.example.workaudio.database

import android.content.Context
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.data.database.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


@RunWith(AndroidJUnit4::class)
class DatabaseTest {


    private lateinit var dao: ApplicationDAO
    private lateinit var db: ApplicationDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ApplicationDatabase::class.java
        ).build()
        dao = db.applicationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_get_latest_workout() = runBlocking {
        dao.clearWorkouts()
        dao.insertWorkout(WorkoutRoomEntity("test", 100, ""))
        val workout = dao.getLatestWorkout()
        assert(workout.name == "test")
    }

    @Test
    fun test_insert_and_get_workout_track_by_id() = runBlocking {
        dao.clearWorkoutsTracks()
        dao.insertWorkoutTrack(
            WorkoutTracksRoomEntity(
                10,
                "abcde",
                "test",
                100,
                "test",
                "test",
                "test"
            )
        )
        val track = dao.getWorkoutTrack(10)
        assert(track.uri == "abcde")
    }


    @Test
    fun test_token() = runBlocking {
        dao.clearToken()
        dao.insertToken(TokenRoomEntity("abc"))
        val tokenFlow = dao.readToken().first().token
        val token = dao.getToken().token
        assert(token == tokenFlow)
    }

    @Test
    fun test_updateWorkoutDefaultImage() = runBlocking {
        dao.clearWorkouts()
        dao.insertWorkout(WorkoutRoomEntity("test", 100, ""))
        dao.updateWorkoutImageUrl("image_url", dao.getLatestWorkout().id)
        assert("image_url" == dao.getLatestWorkout().imageUrl)
    }


    @Test
    fun test_getWorkoutAsFlow() = runBlocking {
        dao.clearWorkouts()
        dao.insertWorkout(WorkoutRoomEntity("test", 100, ""))
        val id = dao.getLatestWorkout().id
        val resultId = dao.getWorkoutById(id).first().id
        assert(id == resultId)
    }


    @Test
    fun test_getWorkoutTracksAsFlow() = runBlocking {
        dao.clearWorkoutsTracks()
        val track = WorkoutTracksRoomEntity(10, "abcde", "test", 100, "test", "test", "test")
        dao.insertWorkoutTrack(track)
        val resultTrack = dao.getWorkoutTracksFlow(10).first()[0]
        assert(track.uri == resultTrack.uri)
    }


}