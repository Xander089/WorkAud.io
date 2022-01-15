package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.usecases.login.LoginBoundary

class FakeLoginService: LoginBoundary {
    override suspend fun insertToken(token: String) {

    }
}