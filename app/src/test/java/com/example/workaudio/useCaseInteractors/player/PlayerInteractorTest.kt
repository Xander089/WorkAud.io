package com.example.workaudio.useCaseInteractors.player

import com.example.workaudio.core.usecases.login.LoginInteractor
import com.example.workaudio.core.usecases.player.PlayerInteractor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlayerInteractorTest {

    private lateinit var interactor: PlayerInteractor
    private lateinit var access: FakePlayerDataAccess

    @Before
    fun setUp() {
        access = FakePlayerDataAccess()
        interactor = PlayerInteractor(access)
    }

    @Test
    fun `when workout is requested by id then it is returned`() = runBlocking {
        val expected = access.list.first()
        val result = interactor.getWorkout(0)
        assertEquals(expected.id, result.id)
    }


}