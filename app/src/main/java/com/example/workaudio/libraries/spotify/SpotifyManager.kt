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

    var mSpotifyAppRemote: SpotifyAppRemote? = null

    fun spotifyDisconnect() = SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    fun logOut(context: Context) = AuthorizationClient.clearCookies(context)

    fun spotifyConnect(context: Context) {

        val connectionParams = ConnectionParams.Builder(context.getString(R.string.client_id))
            .setRedirectUri(context.getString(R.string.callback))
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


    private fun getPlayer() = mSpotifyAppRemote?.playerApi

    //with the track uri as input, this method triggers spotify app to play the related song
    fun play(uri: String) {
        if(uri.isNotEmpty()){
            getPlayer()?.play(uri)
        }
    }

    private fun getTrackInfo(){
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