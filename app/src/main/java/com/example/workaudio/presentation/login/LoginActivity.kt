package com.example.workaudio.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.workaudio.Constants.REQUEST_CODE
import com.example.workaudio.R
import com.example.workaudio.presentation.workoutMainList.MainActivity
import com.example.workaudio.databinding.ActivityLoginBinding
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

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
        val request: AuthorizationRequest = getAuthorizationBuilder().build()
        AuthorizationClient.openLoginActivity(
            this,
            REQUEST_CODE,
            request
        )
    }

    private fun getAuthorizationBuilder(): AuthorizationRequest.Builder {
        val builder: AuthorizationRequest.Builder = AuthorizationRequest.Builder(
            getString(R.string.client_id),
            AuthorizationResponse.Type.TOKEN,
            getString(R.string.callback)
        )
        builder.setScopes(arrayOf(getString(R.string.scopes)))
        return builder
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