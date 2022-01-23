package com.example.workaudio.useCaseInteractors.login

import com.example.workaudio.core.usecases.login.LoginDataAccessInterface

class FakeLoginDataAccess: LoginDataAccessInterface {

    val token = "abc"
    val tokens = mutableListOf(token)

    override suspend fun insertToken(token: String) {
        tokens.add(token)
    }
}