package com.example.workaudio

import android.content.Context
import androidx.room.Room
import com.example.workaudio.data.database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestApplicationModule {

    @Singleton
    @Provides
    @Named("test_db")
    fun provideDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        ApplicationDatabase::class.java, "work_audio_database"
    ).build()


}