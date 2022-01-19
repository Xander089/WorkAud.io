package com.example.workaudio.data.web

import com.example.workaudio.Constants.BEARER
import com.example.workaudio.Constants.SEARCH_TYPE
import okio.IOException
import retrofit2.HttpException

class SpotifyWebService(
    private val api: SpotifyRestApi
) {

    companion object {
        private val emptyResponse = Total(Tracks(emptyList()))
    }

    //a call to the spotify end point to fetch tracks info, given the query text as input
    suspend fun fetchTracks(token: String, queryText: String): Total {

        val safeCall = safeApiCall<Total>("$BEARER $token", queryText) { _token, query ->
            api.getTracks(_token, query, SEARCH_TYPE)
        }

        return when (safeCall) {
            is SafeHandler.NetworkError -> emptyResponse
            is SafeHandler.GenericError -> emptyResponse
            is SafeHandler.Success -> safeCall.value
        }
    }

    private suspend fun <T> safeApiCall(
        token: String,
        query: String,
        service: suspend (String, String) -> T,
    ): SafeHandler<T> =
        try {
            SafeHandler.Success(service(token, query))
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> SafeHandler.NetworkError
                is HttpException -> {
                    SafeHandler.GenericError(
                        throwable.code(),
                        throwable.message()
                    )
                }
                else -> {
                    SafeHandler.GenericError(null, null)
                }
            }

        }


}