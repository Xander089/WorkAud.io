package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.login.LoginViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeLoginBoundary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var boundary: FakeLoginBoundary

    @Before
    fun setup() {
        boundary = FakeLoginBoundary()
        viewModel = LoginViewModel(boundary)
        viewModel.setDispatcher(Dispatchers.Main)
    }


    @Test
    fun when_token_inserted_then_it_is_returned() = runBlocking {
        val expected = "new_token"
        viewModel.cacheSpotifyAuthToken(expected)
        val result = boundary.tokens.first()
        assertEquals(expected, result)

    }


}