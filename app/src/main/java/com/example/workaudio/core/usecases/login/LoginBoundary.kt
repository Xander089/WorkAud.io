package com.example.workaudio.core.usecases.login

interface LoginBoundary{
     suspend fun insertToken(token: String)

}