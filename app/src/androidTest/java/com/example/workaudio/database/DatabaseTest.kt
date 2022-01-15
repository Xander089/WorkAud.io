package com.example.workaudio.database

import com.example.workaudio.data.database.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
class DatabaseTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var db: ApplicationDatabase
    private lateinit var dao: ApplicationDAO

    @Before
    fun setup() {
        hiltRule.inject()
        dao = db.applicationDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun test_insert_token_then_read_it() = runBlocking {
        dao.clearToken()
        dao.insertToken(TokenRoomEntity("token"))
        val tokenRoomEntity = dao.getToken()
        assert("token" == tokenRoomEntity.token)
    }

    @Test
    fun test_insert_and_get_workout_by_id() = runBlocking {
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


}