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
import com.example.workaudio.core.usecases.detail.DetailInteractor
import com.example.workaudio.core.usecases.detail.DetailDataAccess
import com.example.workaudio.core.usecases.player.PlayerBoundary
import com.example.workaudio.core.usecases.searchTracks.SearchBoundary
import com.example.workaudio.core.usecases.workoutList.ListInteractor
import com.example.workaudio.core.usecases.workoutList.ListDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import com.example.workaudio.core.usecases.workoutList.ListBoundary
import com.example.workaudio.libraries.spotify.SpotifyManager
import com.example.workaudio.presentation.player.PlayerViewModel
import com.example.workaudio.presentation.searchTracks.SearchTracksFragmentViewModel
import com.example.workaudio.presentation.utils.timer.TimerFactoryImpl
import com.example.workaudio.presentation.workoutMainList.WorkoutListFragmentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    private const val ENDPOINT = "https://api.spotify.com/v1/"

    @Singleton
    @Provides
    fun provideManager() = SpotifyManager()


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
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
        ListDataAccess(db.applicationDao())

    @Singleton
    @Provides
    fun provideNavigation(dataAccess: ListDataAccess) =
        ListInteractor(dataAccess)

    @Singleton
    @Provides
    fun provideEditingDataAccess(db: ApplicationDatabase) =
        DetailDataAccess(db.applicationDao())

    @Singleton
    @Provides
    fun provideEditing(dataAccess: DetailDataAccess) =
        DetailInteractor(dataAccess)

    @Singleton
    @Provides
    fun provideCreationDataAccessImpl(db: ApplicationDatabase) =
        CreationDataAccess(db.applicationDao())

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

    @Provides
    fun provideSearchViewModel(boundary: SearchBoundary) =
        SearchTracksFragmentViewModel(boundary)

    @Singleton
    @Provides
    fun provideTimer() = TimerFactoryImpl()

    @Provides
    fun provideMainListViewModel(boundary: ListBoundary) =
        WorkoutListFragmentViewModel(boundary)

    @Provides
    fun providePlayerViewModel(boundary: PlayerBoundary) =
        PlayerViewModel(boundary)
}