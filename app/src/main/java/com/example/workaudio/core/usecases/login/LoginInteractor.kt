package com.example.workaudio.core.usecases.login


class LoginInteractor(
    private val dataAccess: LoginDataAccessInterface
) : LoginBoundary {

    override suspend fun insertToken(token: String) = dataAccess.insertToken(token)

}