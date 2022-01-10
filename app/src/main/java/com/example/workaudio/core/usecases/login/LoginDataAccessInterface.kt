package com.example.workaudio.core.usecases.login

interface LoginDataAccessInterface {

    suspend fun insertToken(token: String)
}