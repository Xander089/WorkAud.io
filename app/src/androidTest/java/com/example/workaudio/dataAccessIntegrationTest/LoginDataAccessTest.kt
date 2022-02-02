package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.core.usecases.login.LoginDataAccess
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class LoginDataAccessTest {

    private lateinit var dataAccess: LoginDataAccess
    private lateinit var dao: ApplicationDAO

    @Before
    fun createDb() {
        dao = TestDatabaseFactory.createDao()!!
        dataAccess = LoginDataAccess(dao)

        runBlocking {
            dao.clearToken()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        TestDatabaseFactory.disposeDb()
    }


    @Test
    fun whenTokenInserted_thenItCanBeRead() = runBlocking {
        //Given
        val expected = "new_token"
        //When
        dataAccess.insertToken(expected)
        val actual = dao.getToken()?.token.orEmpty()
        //Then
        assertEquals(expected, actual)
        dao.clearToken()

    }

}