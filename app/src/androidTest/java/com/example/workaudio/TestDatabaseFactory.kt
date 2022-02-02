package com.example.workaudio

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.ApplicationDatabase

object TestDatabaseFactory {

    private var applicationDatabase: ApplicationDatabase? = null

    fun createDao(): ApplicationDAO? {
        if (applicationDatabase == null) {
            applicationDatabase = createDb()
        }
        return applicationDatabase?.applicationDao()
    }

    private fun createDb(): ApplicationDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(
            context,
            ApplicationDatabase::class.java
        ).build()
    }

    fun disposeDb() {
        applicationDatabase?.close()
        applicationDatabase = null
    }
}