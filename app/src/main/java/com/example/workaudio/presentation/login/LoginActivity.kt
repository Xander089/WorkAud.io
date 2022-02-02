package com.example.workaudio.presentation.login

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.workaudio.common.Constants.REQUEST_CODE
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
        super.onCreate(savedInstanceState)
        automaticLogin(isLoginAutomatic())
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener { authenticateSpotify() }
        binding.dontShowAnymoreCheckBox.isChecked = isLoginAutomatic()


    }

    private fun automaticLogin(isAutomatic: Boolean) {
        if (isAutomatic) {
            authenticateSpotify()
        }
    }

    private fun isLoginAutomatic(): Boolean =
        getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.login_pref), false)

    private fun saveLoginAction(doNotShowAnymore: Boolean) {
        if (isLoginAutomatic()) {
            return
        }

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.login_pref), doNotShowAnymore)
            apply()
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
            getSpotifyClientId(),
            AuthorizationResponse.Type.TOKEN,
            getString(R.string.callback)
        )
        builder.setScopes(arrayOf(getString(R.string.scopes)))
        return builder
    }

    private fun getSpotifyClientId(): String {
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        return ai.metaData["clientId"].toString()
    }


    /*
    Despite being deprecated, this is necessary since the activity result is fed by openLoginActivity,
    a method from the spotify sdk used to authenticate with Spotify.
    Hopefully, this will be updated in future spotify sdk releases
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_CODE) {
            val response: AuthorizationResponse =
                AuthorizationClient.getResponse(resultCode, intent)
            response.accessToken?.let { token ->
                saveLoginAction(binding.dontShowAnymoreCheckBox.isChecked)
                viewModel.cacheSpotifyAuthToken(token)
                startActivity(MainActivity.newIntent(this))
                finish()
            }

        }
        super.onActivityResult(requestCode, resultCode, intent)
    }

}