package com.example.workaudio.dataAccessIntegrationTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    private lateinit var db: ApplicationDatabase
    private lateinit var dao: ApplicationDAO

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ApplicationDatabase::class.java
        ).build()
        dao = db.applicationDao()
        dataAccess = LoginDataAccess(dao)


        runBlocking {
            dao.clearToken()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun when_token_inserted_then_when_requested_it_is_returned() = runBlocking {
        val token = "new_token"
        dataAccess.insertToken(token)
        val result = dao.getToken()?.token.orEmpty()
        dao.clearToken()
        assertEquals(token,result)

    }



}