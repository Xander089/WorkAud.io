package com.example.workaudio.data.web

import io.reactivex.rxjava3.core.Observable
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


    @GET("search")
     fun getTracksAsObservable(
        @Header("Authorization") token: String,
        @Query("q") track: String,
        @Query("type") type: String,

        ): Observable<Total>
}