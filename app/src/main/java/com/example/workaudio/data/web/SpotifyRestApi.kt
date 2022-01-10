package com.example.workaudio.data.web

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyRestApi {

    @GET("search")
    suspend fun getTracks(
        @Header("Authorization") token: String,
        @Query("q") track: String,
        @Query("type") type: String,

    ): Total
}