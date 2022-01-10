package com.example.workaudio

import com.example.workaudio.data.web.SpotifyRestApi
import com.example.workaudio.data.web.SpotifyWebService
import org.junit.Test

import org.junit.Assert.*
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

import okhttp3.OkHttpClient


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    object ApiService {
        private val YOUR_TOKEN =
            "BQBTZTq2shkS-YTD4ANpdIhei-59FQokRcfsh_RBo5s9ON5erV9f059QLNHuUhZfShfnbgKKmwRVK4VMQ428jep71_E8JRaaoYDZvMKfKW0JMe7CN_0b0go6y9nsF09PdSVdZc1sdvEweaVNwyR1h3tvd1GJkNyT"

        private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request =
                    chain.request().newBuilder().addHeader("Authorization", "Bearer ${YOUR_TOKEN}")
                        .build()
                chain.proceed(request)
            }.build())
            .build()
        private val api = retrofit.create(SpotifyRestApi::class.java)
        val service = SpotifyWebService(api)


    }

    @Test
    fun ottengo_qualcosa() {
        assert(ApiService.service.get())
    }
}