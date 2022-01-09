package com.example.workaudio.core.usecases.login

interface LoginServiceBoundary{
     suspend fun insertToken(token: String)

}