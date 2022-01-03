package com.example.workaudio.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.workaudio.presentation.navigation.MainActivity
import com.example.workaudio.R
import com.example.workaudio.databinding.ActivityLoginBinding
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    companion object {
        private const val CLIENT_ID = "522adc8dc0d643bea4200d7e3721dbc5"
        private const val REDIRECT_URI = "http://10.0.2.2:8090/callback"
        private const val REQUEST_CODE = 1337
        private const val SCOPES =
            "user-read-recently-played,user-library-modify,user-read-email,user-read-private"
    }


    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            authenticateSpotify()
        }


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

            response.accessToken?.let { token ->
                viewModel.cacheSpotifyAuthToken(token)
                startActivity(MainActivity.newIntent(this))
                finish()
            }

        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

}