package com.example.workaudio.core.usecases.login


class LoginInteractor(private val facade: LoginFacade) {

    suspend fun insertToken(token: String) = facade.insertToken(token)

}