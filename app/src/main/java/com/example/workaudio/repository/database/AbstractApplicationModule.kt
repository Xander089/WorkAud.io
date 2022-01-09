package com.example.workaudio.repository.database

import com.example.workaudio.core.usecases.creation.CreationServiceBoundary
import com.example.workaudio.core.usecases.creation.WorkoutCreationInteractor
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
    abstract fun bindCurrentRunBoundary(interactor: WorkoutCreationInteractor): CreationServiceBoundary

//    @Singleton
//    @Binds
//    abstract fun bindCurrentRunDataAccessInterface(dataAccessImpl: RunInfoDataAccessImpl): RunInfoDataAccessInterface

}