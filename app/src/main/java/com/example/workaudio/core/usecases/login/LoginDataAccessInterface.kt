package com.example.workaudio.core.usecases.login

import com.example.workaudio.repository.database.TokenRoomEntity

interface LoginDataAccessInterface {

    suspend fun insertToken(token: String)
}