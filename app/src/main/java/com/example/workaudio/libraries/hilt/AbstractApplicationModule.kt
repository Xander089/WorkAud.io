package com.example.workaudio.libraries.hilt

import com.example.workaudio.core.usecases.creation.CreationDataAccess
import com.example.workaudio.core.usecases.creation.CreationDataAccessInterface
import com.example.workaudio.core.usecases.creation.CreationServiceBoundary
import com.example.workaudio.core.usecases.creation.CreationInteractor
import com.example.workaudio.core.usecases.editing.EditingDataAccess
import com.example.workaudio.core.usecases.editing.EditingDataAccessInterface
import com.example.workaudio.core.usecases.editing.EditingInteractor
import com.example.workaudio.core.usecases.editing.EditingServiceBoundary
import com.example.workaudio.core.usecases.login.LoginDataAccess
import com.example.workaudio.core.usecases.login.LoginDataAccessInterface
import com.example.workaudio.core.usecases.login.LoginInteractor
import com.example.workaudio.core.usecases.login.LoginServiceBoundary
import com.example.workaudio.core.usecases.navigation.NavigationDataAccess
import com.example.workaudio.core.usecases.navigation.NavigationDataAccessInterface
import com.example.workaudio.core.usecases.navigation.NavigationInteractor
import com.example.workaudio.core.usecases.navigation.NavigationServiceBoundary
import com.example.workaudio.core.usecases.player.PlayerDataAccess
import com.example.workaudio.core.usecases.player.PlayerDataAccessInterface
import com.example.workaudio.core.usecases.player.PlayerInteractor
import com.example.workaudio.core.usecases.player.PlayerServiceBoundary
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccess
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccessInterface
import com.example.workaudio.core.usecases.searchTracks.SearchInteractor
import com.example.workaudio.core.usecases.searchTracks.SearchServiceBoundary
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
    abstract fun bindCreationBoundary(interactor: CreationInteractor): CreationServiceBoundary

    @Singleton
    @Binds
    abstract fun bindCreationDataAccessInterface(dataAccessImpl: CreationDataAccess): CreationDataAccessInterface


    @Singleton
    @Binds
    abstract fun bindEditingBoundary(interactor: EditingInteractor): EditingServiceBoundary

    @Singleton
    @Binds
    abstract fun bindEditingDataAccessInterface(dataAccessImpl: EditingDataAccess): EditingDataAccessInterface


    @Singleton
    @Binds
    abstract fun bindLoginBoundary(interactor: LoginInteractor): LoginServiceBoundary

    @Singleton
    @Binds
    abstract fun bindLoginDataAccessInterface(dataAccessImpl: LoginDataAccess): LoginDataAccessInterface

    @Singleton
    @Binds
    abstract fun bindSearchBoundary(interactor: SearchInteractor): SearchServiceBoundary

    @Singleton
    @Binds
    abstract fun bindSearchDataAccessInterface(dataAccessImpl: SearchDataAccess): SearchDataAccessInterface

    @Singleton
    @Binds
    abstract fun bindPlayerBoundary(interactor: PlayerInteractor): PlayerServiceBoundary

    @Singleton
    @Binds
    abstract fun bindPlayerDataAccessInterface(dataAccessImpl: PlayerDataAccess): PlayerDataAccessInterface


    @Singleton
    @Binds
    abstract fun bindNavigationBoundary(interactor: NavigationInteractor): NavigationServiceBoundary

    @Singleton
    @Binds
    abstract fun bindNavigationDataAccessInterface(dataAccessImpl: NavigationDataAccess): NavigationDataAccessInterface


}