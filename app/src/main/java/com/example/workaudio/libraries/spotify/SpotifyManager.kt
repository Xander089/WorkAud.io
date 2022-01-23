package com.example.workaudio.libraries.spotify

import android.content.Context
import com.example.workaudio.Constants.RESET_TIME
import com.example.workaudio.R
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.sdk.android.auth.AuthorizationClient

class SpotifyManager() {

    // spotifyAppRemote let us remotely control Spotify from within this app
    // For example: pause-resume the player, or play a specific song
    var mSpotifyAppRemote: SpotifyAppRemote? = null

    fun spotifyDisconnect() = SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    fun logOut(context: Context) = AuthorizationClient.clearCookies(context)

    fun spotifyConnect(context: Context, clientId: String = "") {
        SpotifyAppRemote.connect(
            context,
            getConnectionParams(context,clientId),
            object : ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    if (mSpotifyAppRemote == null) {
                        mSpotifyAppRemote = spotifyAppRemote
                    }
                }

                override fun onFailure(throwable: Throwable) {}
            })
    }

    private fun getConnectionParams(
        context: Context,
        clientId: String
    ) = ConnectionParams.Builder(clientId)
        .setRedirectUri(context.getString(R.string.callback))
        .showAuthView(true)
        .build()

    private fun getPlayer() = mSpotifyAppRemote?.playerApi

    //with the track uri as input, this method triggers spotify app to play the related song
    fun play(songUri: String) {
        if (songUri.isNotEmpty()) {
            getPlayer()?.play(songUri)
        }
    }

    private fun getTrackInfo() {
        getPlayer()?.subscribeToPlayerState()?.setEventCallback { playerState: PlayerState -> }
    }

    fun pausePlayer() = getPlayer()?.pause()

    fun resumePlayer() = getPlayer()?.resume()

    fun stopSpotifyPlayer(timerText: String) {
        val resetTimeText = RESET_TIME
        if (timerText == resetTimeText) {
            pausePlayer()
        }
    }


}