package com.example.workaudio.repository.web

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface SpotifyRestApi {

    @GET("search")
    suspend fun getTracks(
        @Header("Authorization") token: String,
        @Query("q") track: String,
        @Query("type") type: String,

    ): Total
}