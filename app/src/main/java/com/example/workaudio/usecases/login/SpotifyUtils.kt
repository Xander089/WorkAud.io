package com.example.workaudio.usecases.login

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.sdk.android.auth.AuthorizationClient

object SpotifyUtils {

    const val CLIENT_ID = "522adc8dc0d643bea4200d7e3721dbc5"
    const val REDIRECT_URI = "http://10.0.2.2:8090/callback"
    const val REQUEST_CODE = 1337
    const val SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private"

    var mSpotifyAppRemote: SpotifyAppRemote? = null

    fun spotifyDisconnect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    fun spotifyConnect(context: Context) {

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams,
            object : ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                }

                override fun onFailure(throwable: Throwable) {}
            })
    }

    fun play(uri: String) {

        mSpotifyAppRemote!!.playerApi.play(uri)
        mSpotifyAppRemote!!.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState: PlayerState ->
                val track = playerState.track
                if (track != null) {

                }
            }
    }

    fun logOut(context: Context) {
        AuthorizationClient.clearCookies(context)
    }


}