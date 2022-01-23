package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.core.usecases.login.LoginBoundary

class FakeLoginBoundary: LoginBoundary {

    val tokens = mutableListOf<String>()
    override suspend fun insertToken(token: String) {
        tokens.add(token)
    }
}