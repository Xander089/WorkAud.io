package com.example.workaudio.core.usecases.login


class LoginInteractor(
    private val dataAccess: LoginDataAccessInterface
) : LoginServiceBoundary {

    override suspend fun insertToken(token: String) = dataAccess.insertToken(token)

}