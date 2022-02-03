package com.example.workaudio.useCaseInteractors

import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.login.LoginDataAccessInterface
import com.example.workaudio.core.usecases.login.LoginInteractor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginInteractorTest {

    private lateinit var interactor: LoginInteractor

    @Mock
    lateinit var access: LoginDataAccessInterface
    private lateinit var source: TestDataSource


    @Before
    fun setUp() {
        source = TestDataSource()
        interactor = LoginInteractor(access)
    }

    @Test
    fun whenNewTokenInserted_thenItIsCached() = runBlocking {

        //Given
        val token = "new_token"

        //When
        Mockito.`when`(access.insertToken(token)).thenReturn(addToken(token))
        interactor.insertToken(token)

        //Then
        val actual = source.tokens.last()
        assertEquals(token, actual)
    }


    private fun addToken(token: String) {
        source.tokens.add(token)
    }


}