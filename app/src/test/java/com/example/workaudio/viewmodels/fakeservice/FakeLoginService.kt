package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.usecases.login.LoginServiceBoundary

class FakeLoginService: LoginServiceBoundary {
    override suspend fun insertToken(token: String) {

    }
}