package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.core.usecases.login.LoginBoundary

class FakeLoginBoundary: LoginBoundary {
    override suspend fun insertToken(token: String) {

    }
}