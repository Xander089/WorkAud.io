package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.workaudio.core.usecases.login.LoginBoundary
import com.example.workaudio.presentation.login.LoginViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var viewModel: LoginViewModel

    @Mock
    lateinit var boundary: LoginBoundary

    private lateinit var source : TestDataSource


    @Before
    fun setup() {
        source = TestDataSource()
        hiltRule.inject()

        viewModel = LoginViewModel(boundary)
        viewModel.setDispatcher(Dispatchers.Main)
    }

    private fun insert(token: String) {
        source.tokens.add(token)
    }


    @Test
    fun when_tokenIsInserted_then_itIsReturned() = runBlocking(Dispatchers.Main) {
        val expected = "new_token"
        Mockito.`when`(boundary.insertToken(expected)).thenReturn(insert(expected))
        viewModel.cacheSpotifyAuthToken(expected)
        delay(1000)
        val actual = source.tokens.first()
        assertEquals(expected, actual)
    }


}