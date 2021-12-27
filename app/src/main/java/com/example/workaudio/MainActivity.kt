package com.example.workaudio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.extras?.getString(SPOTIFY_TOKEN).also {
            Log.v("da main:", it!!)
        }
    }

    companion object {
        const val SPOTIFY_TOKEN = "SPOTIFY_TOKEN"

        @JvmStatic
        fun newIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}