package com.example.workaudio.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDataSource
import com.example.workaudio.data.database.*
import com.example.workaudio.TestDatabaseFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var dao: ApplicationDAO
    private lateinit var source: TestDataSource

    @Before
    fun createDb() {
        source = TestDataSource()
        dao = TestDatabaseFactory.createDao()!!
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        TestDatabaseFactory.disposeDb()
    }

    private suspend fun insertWorkout() = dao.insertWorkout(source.workoutRoomEntity)
    private suspend fun insertWorkoutTrack() =
        dao.insertWorkoutTrack(source.workoutTracksRoomEntity)


    @Test
    fun whenWorkoutEntityIsCreated_thenItIsCachedInTheDbAndCanBeRead() = runBlocking {
        val expected = "test_name"
        dao.clearWorkouts()
        insertWorkout()
        val actual = dao.getLatestWorkout()?.name
        assertEquals(expected, actual)
    }

    @Test
    fun whenWorkoutTrackEntityIsCreated_thenItIsCachedInTheDbAndCanBeReadByWorkoutId() =
        runBlocking {
            val expected = "track_uri"
            dao.clearWorkoutsTracks()
            insertWorkoutTrack()
            val actual = dao.getWorkoutTrack(10)?.uri
            assertEquals(expected, actual)
        }


    @Test
    fun whenTokenCreated_thenItIsCachedAndCanBeRead() = runBlocking {
        val expected = "token"
        dao.clearToken()
        dao.insertToken(source.token)
        val actual = dao.getToken()?.token
        assertEquals(expected, actual)
    }

    @Test
    fun whenTokenCreated_thenItIsCachedAndCanBeReadAsFlow() = runBlocking {
        val expected = "token"
        dao.clearToken()
        dao.insertToken(source.token)
        val actual = dao.readToken().first()?.token
        assertEquals(expected, actual)
    }

    @Test
    fun whenImageUrlUpdatedAs_newImageUrl_thenItCanBeReadAs_newImageUrl() = runBlocking {
        val expected = "new_image_url"
        dao.clearWorkouts()
        insertWorkout()
        dao.getLatestWorkout()?.id?.let { id ->
            dao.updateWorkoutImageUrl(expected, id)
        }
        val actual = dao.getLatestWorkout()?.imageUrl
        assertEquals(expected, actual)
    }


    @Test
    fun whenWorkoutTrackEntityIsCreated_thenItIsCachedInTheDbAndCanBeReadByWorkoutIdAsFlow() = runBlocking {
        val expected = "track_uri"
        dao.clearWorkoutsTracks()
        insertWorkoutTrack()
        val actual = dao.getWorkoutTracksFlow(10).first()?.get(0)?.uri
        assertEquals(expected, actual)
    }


}