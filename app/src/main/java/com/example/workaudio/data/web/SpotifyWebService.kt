package com.example.workaudio.data.web

class SpotifyWebService(
    private val api: SpotifyRestApi
) {

    suspend fun fetchTracks(token: String, queryText: String) : Total {
        return api.getTracks("Bearer $token", queryText, "track")
    }

}