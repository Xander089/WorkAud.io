package com.example.workaudio.data.web

import com.example.workaudio.Constants.BEARER
import com.example.workaudio.Constants.SEARCH_TYPE

class SpotifyWebService(
    private val api: SpotifyRestApi
) {

    //a call to the spotify end point to fetch tracks info, given the query text as input
    suspend fun fetchTracks(token: String, queryText: String) : Total {
        return api.getTracks("$BEARER $token", queryText, SEARCH_TYPE)
    }

}