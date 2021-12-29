package com.example.workaudio.hilt

import android.content.Context
import androidx.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.workaudio.repository.database.ApplicationDatabase
import com.example.workaudio.repository.web.SpotifyRestApi
import com.example.workaudio.repository.web.SpotifyWebService
import com.example.workaudio.usecases.login.Login
import com.example.workaudio.usecases.login.LoginFacade
import com.example.workaudio.usecases.workoutCreation.WorkoutCreation
import com.example.workaudio.usecases.workoutCreation.WorkoutCreationFacade
import com.example.workaudio.usecases.workoutEditing.WorkoutEditing
import com.example.workaudio.usecases.workoutEditing.WorkoutEditingFacade
import com.example.workaudio.usecases.workoutNavigation.WorkoutNavigation
import com.example.workaudio.usecases.workoutNavigation.WorkoutNavigationFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    companion object {
        private const val ENDPOINT = "https://api.spotify.com/v1/"
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient()

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideAPI(retrofit: Retrofit): SpotifyRestApi = retrofit.create(SpotifyRestApi::class.java)

    @Singleton
    @Provides
    fun provideNetwork(api: SpotifyRestApi) = SpotifyWebService(api)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        ApplicationDatabase::class.java, "work_audio_database"
    ).build()


    @Singleton
    @Provides
    fun provideWorkoutNavigationFacade(db: ApplicationDatabase) =
        WorkoutNavigationFacade(db.applicationDao())

    @Singleton
    @Provides
    fun provideWorkoutNavigation(facade: WorkoutNavigationFacade) =
        WorkoutNavigation(facade)

    @Singleton
    @Provides
    fun provideWorkoutEditingFacade(db: ApplicationDatabase) =
        WorkoutEditingFacade(db.applicationDao())

    @Singleton
    @Provides
    fun provideWorkoutEditing(facade: WorkoutEditingFacade) =
        WorkoutEditing(facade)

    @Singleton
    @Provides
    fun provideWorkoutCreationFacade(db: ApplicationDatabase, service: SpotifyWebService) =
        WorkoutCreationFacade(db.applicationDao(), service)

    @Singleton
    @Provides
    fun provideWorkoutCreation(facade: WorkoutCreationFacade) =
        WorkoutCreation(facade)


    @Singleton
    @Provides
    fun provideLoginFacade(db: ApplicationDatabase) =
        LoginFacade(db.applicationDao())

    @Singleton
    @Provides
    fun provideLogin(facade: LoginFacade) =
        Login(facade)


}