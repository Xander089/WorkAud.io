package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.login.LoginViewModel
import com.example.workaudio.viewmodels.fakeservice.FakeLoginService
import org.junit.Before
import org.junit.Test


class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(FakeLoginService())
    }


    @Test
    fun test() {

    }


}