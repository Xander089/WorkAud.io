package com.example.workaudio.repository.web

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Query

class SpotifyWebService(
    private val api: SpotifyRestApi
) {

    suspend fun fetchTracks(token: String, queryText: String) : Total {
        return api.getTracks("Bearer $token", queryText, "track")
    }

}