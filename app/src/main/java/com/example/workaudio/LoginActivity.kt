package com.example.workaudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticateSpotify()
    }

    private fun authenticateSpotify() {

        val builder: AuthorizationRequest.Builder = AuthorizationRequest.Builder(
            CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            REDIRECT_URI
        )
        builder.setScopes(arrayOf<String>(SCOPES))
        val request: AuthorizationRequest = builder.build()

        AuthorizationClient.openLoginActivity(
            this,
            REQUEST_CODE,
            request
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse =
                AuthorizationClient.getResponse(resultCode, intent)
            if (response.accessToken != null) {
                Log.v("from login",response.accessToken.toString())
                val intent = MainActivity.newIntent(this)
                intent.putExtra(MainActivity.SPOTIFY_TOKEN, response.accessToken)
                startActivity(intent)
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

    companion object {
        const val CLIENT_ID = "522adc8dc0d643bea4200d7e3721dbc5"
        const val REDIRECT_URI = "http://10.0.2.2:8090/callback"
        const val REQUEST_CODE = 1337
        const val SCOPES =
            "user-read-recently-played,user-library-modify,user-read-email,user-read-private"
    }
}