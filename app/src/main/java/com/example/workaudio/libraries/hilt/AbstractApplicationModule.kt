package com.example.workaudio.libraries.hilt

import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.core.usecases.creation.CreationDataAccessInterface
import com.example.workaudio.core.usecases.creation.CreationBoundary
import com.example.workaudio.core.usecases.creation.CreationInteractor
import com.example.workaudio.core.usecases.detail.DetailDataAccess
import com.example.workaudio.core.usecases.detail.DetailDataAccessInterface
import com.example.workaudio.core.usecases.detail.DetailInteractor
import com.example.workaudio.core.usecases.detail.DetailBoundary
import com.example.workaudio.core.usecases.login.LoginDataAccess
import com.example.workaudio.core.usecases.login.LoginDataAccessInterface
import com.example.workaudio.core.usecases.login.LoginInteractor
import com.example.workaudio.core.usecases.login.LoginBoundary
import com.example.workaudio.core.usecases.workoutList.ListDataAccess
import com.example.workaudio.core.usecases.workoutList.ListDataAccessInterface
import com.example.workaudio.core.usecases.workoutList.ListInteractor
import com.example.workaudio.core.usecases.workoutList.ListBoundary
import com.example.workaudio.core.usecases.player.PlayerDataAccess
import com.example.workaudio.core.usecases.player.PlayerDataAccessInterface
import com.example.workaudio.core.usecases.player.PlayerInteractor
import com.example.workaudio.core.usecases.player.PlayerBoundary
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccessInterface
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import com.example.workaudio.core.usecases.searchTracks.SearchBoundary
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractApplicationModule {

    @Singleton
    @Binds
    abstract fun bindCreationBoundary(interactor: CreationInteractor): CreationBoundary

    @Singleton
    @Binds
    abstract fun bindCreationDataAccessInterface(dataAccessImpl: CreationDataAccess): CreationDataAccessInterface


    @Singleton
    @Binds
    abstract fun bindEditingBoundary(interactor: DetailInteractor): DetailBoundary

    @Singleton
    @Binds
    abstract fun bindEditingDataAccessInterface(dataAccessImpl: DetailDataAccess): DetailDataAccessInterface


    @Singleton
    @Binds
    abstract fun bindLoginBoundary(interactor: LoginInteractor): LoginBoundary

    @Singleton
    @Binds
    abstract fun bindLoginDataAccessInterface(dataAccessImpl: LoginDataAccess): LoginDataAccessInterface

    @Singleton
    @Binds
    abstract fun bindSearchBoundary(interactor: SearchInteractor): SearchBoundary

    @Singleton
    @Binds
    abstract fun bindSearchDataAccessInterface(dataAccessImpl: SearchDataAccess): SearchDataAccessInterface

    @Singleton
    @Binds
    abstract fun bindPlayerBoundary(interactor: PlayerInteractor): PlayerBoundary

    @Singleton
    @Binds
    abstract fun bindPlayerDataAccessInterface(dataAccessImpl: PlayerDataAccess): PlayerDataAccessInterface


    @Singleton
    @Binds
    abstract fun bindNavigationBoundary(interactor: ListInteractor): ListBoundary

    @Singleton
    @Binds
    abstract fun bindNavigationDataAccessInterface(dataAccessImpl: ListDataAccess): ListDataAccessInterface


}