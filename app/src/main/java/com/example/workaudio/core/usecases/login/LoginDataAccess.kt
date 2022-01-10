package com.example.workaudio.core.usecases.login

import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.database.TokenRoomEntity

class LoginDataAccess(private val dao: ApplicationDAO) : LoginDataAccessInterface {

    override suspend fun insertToken(token: String) {
        dao.clearToken()
        val entity = TokenRoomEntity(token)
        dao.insertToken(entity)
    }

}