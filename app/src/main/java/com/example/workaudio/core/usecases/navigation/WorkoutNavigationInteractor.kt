package com.example.workaudio.core.usecases.navigation

class WorkoutNavigationInteractor(
    private val facade: WorkoutNavigationFacade
) {
    val workouts  = facade.workouts
}