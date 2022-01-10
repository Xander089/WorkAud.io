package com.example.workaudio.libraries.hilt

import android.content.Context
import androidx.room.Room
import com.example.workaudio.data.database.ApplicationDatabase
import com.example.workaudio.data.web.SpotifyRestApi
import com.example.workaudio.data.web.SpotifyWebService
import com.example.workaudio.core.usecases.login.LoginInteractor
import com.example.workaudio.core.usecases.login.LoginDataAccess
import com.example.workaudio.core.usecases.player.PlayerInteractor
import com.example.workaudio.core.usecases.player.PlayerDataAccess
import com.example.workaudio.core.usecases.creation.CreationInteractor
import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.core.usecases.editing.EditingInteractor
import com.example.workaudio.core.usecases.editing.EditingDataAccess
import com.example.workaudio.core.usecases.navigation.NavigationInteractor
import com.example.workaudio.core.usecases.navigation.NavigationDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
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
    fun provideNavigationDataAccess(db: ApplicationDatabase) =
        NavigationDataAccess(db.applicationDao())

    @Singleton
    @Provides
    fun provideNavigation(dataAccess: NavigationDataAccess) =
        NavigationInteractor(dataAccess)

    @Singleton
    @Provides
    fun provideEditingDataAccess(db: ApplicationDatabase) =
        EditingDataAccess(db.applicationDao())

    @Singleton
    @Provides
    fun provideEditing(dataAccess: EditingDataAccess) =
        EditingInteractor(dataAccess)

    @Singleton
    @Provides
    fun provideCreationDataAccessImpl(db: ApplicationDatabase, service: SpotifyWebService) =
        CreationDataAccess(db.applicationDao(), service)

    @Singleton
    @Provides
    fun provideCreationInteractor(dataAccess: CreationDataAccess) =
        CreationInteractor(dataAccess)


    @Singleton
    @Provides
    fun provideLoginDataAccess(db: ApplicationDatabase) =
        LoginDataAccess(db.applicationDao())

    @Singleton
    @Provides
    fun provideLogin(dataAccess: LoginDataAccess) =
        LoginInteractor(dataAccess)

    @Singleton
    @Provides
    fun providePlayerDataAccess(db: ApplicationDatabase) =
        PlayerDataAccess(db.applicationDao())

    @Singleton
    @Provides
    fun providePlayer(dataAccess: PlayerDataAccess) =
        PlayerInteractor(dataAccess)

    @Singleton
    @Provides
    fun provideSearchDataAccess(db: ApplicationDatabase, service: SpotifyWebService) =
        SearchDataAccess(db.applicationDao(), service)

    @Singleton
    @Provides
    fun provideSearch(dataAccess: SearchDataAccess) =
        SearchInteractor(dataAccess)


}