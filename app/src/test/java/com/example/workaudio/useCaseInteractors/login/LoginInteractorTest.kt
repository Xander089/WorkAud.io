package com.example.workaudio.useCaseInteractors.login

import com.example.workaudio.core.usecases.login.LoginInteractor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginInteractorTest {

    private lateinit var interactor: LoginInteractor
    private lateinit var access: FakeLoginDataAccess

    @Before
    fun setUp() {
        access = FakeLoginDataAccess()
        interactor = LoginInteractor(access)
    }

    @Test
    fun `when a new token is inserted, then it is cached in repository`() = runBlocking {
        val token = "new_token"
        interactor.insertToken(token)
        val result = access.tokens.last()
        assertEquals(token, result)
    }


}