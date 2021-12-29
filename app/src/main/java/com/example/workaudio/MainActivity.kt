package com.example.workaudio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.workaudio.usecases.workoutCreation.WorkoutCreation
import com.example.workaudio.usecases.workoutCreation.WorkoutCreationViewModel
import com.spotify.sdk.android.auth.AuthorizationClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {

        @JvmStatic
        fun newIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

    override fun onStop() {
        super.onStop()
        AuthorizationClient.clearCookies(this)
    }
}