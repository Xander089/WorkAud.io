package com.example.workaudio.core.usecases.login

abstract class LoginServiceBoundary(
    open val facade: LoginFacade
) {
    abstract suspend fun insertToken(token: String)

}