package com.example.workaudio.usecases.login


class Login(private val facade: LoginFacade) {

    suspend fun insertToken(token: String) = facade.insertToken(token)

}