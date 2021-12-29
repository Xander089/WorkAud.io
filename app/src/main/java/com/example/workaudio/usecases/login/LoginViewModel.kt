package com.example.workaudio.usecases.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.usecases.login.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val login: Login) :
    ViewModel() {

    fun insertToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            login.insertToken(token)
        }
    }

}