package com.example.workaudio.core.usecases.login


class LoginInteractor(override val facade: LoginFacade): LoginServiceBoundary(facade) {

    override suspend fun insertToken(token: String) = facade.insertToken(token)

}