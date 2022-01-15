package com.example.workaudio.presentation.utils.itemTouchHelper

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

object NavigationManager {

    fun navigateTo(
        navController: NavController,
        action: Int,
        bundle: Bundle? = null
    ) {
        if (bundle == null) {
            navController.navigate(
                action
            )
        } else {
            navController.navigate(
                action,
                bundle
            )
        }

    }
}