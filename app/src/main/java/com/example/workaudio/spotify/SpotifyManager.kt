package com.example.workaudio.spotify

import android.content.Context
import com.example.workaudio.R
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.sdk.android.auth.AuthorizationClient

class SpotifyManager() {

    companion object {
        const val CLIENT_ID = "522adc8dc0d643bea4200d7e3721dbc5"
        const val REDIRECT_URI = "http://10.0.2.2:8090/callback"
        private const val RESET_TIME = "00:00:00"
    }


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
                    if (mSpotifyAppRemote == null) {
                        mSpotifyAppRemote = spotifyAppRemote
                    }

                }

                override fun onFailure(throwable: Throwable) {}
            })
    }


    fun play(uri: String) {

        if (uri.isEmpty()) {
            return
        }

        val playerApi = mSpotifyAppRemote?.playerApi
        playerApi?.play(uri)
        playerApi?.subscribeToPlayerState()?.setEventCallback { playerState: PlayerState -> }
    }


    fun logOut(context: Context) {
        AuthorizationClient.clearCookies(context)
    }

    fun pausePlayer() = mSpotifyAppRemote?.playerApi?.pause()
    fun resumePlayer() = mSpotifyAppRemote?.playerApi?.resume()
    fun stopSpotifyPlayer(timerText: String) {

        val resetTimeText = RESET_TIME
        if (timerText == resetTimeText) {
            mSpotifyAppRemote?.playerApi?.pause()
        }
    }


}