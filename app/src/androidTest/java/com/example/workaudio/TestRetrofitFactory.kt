package com.example.workaudio

import com.example.workaudio.data.web.SpotifyRestApi
import com.example.workaudio.data.web.SpotifyWebService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestRetrofitFactory {

    private const val ENDPOINT = "https://api.spotify.com/v1/"

    fun createWebService(): SpotifyWebService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(SpotifyRestApi::class.java)
        return SpotifyWebService(api)
    }

}