package com.example.workaudio.presentation.workoutMainList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workaudio.R
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