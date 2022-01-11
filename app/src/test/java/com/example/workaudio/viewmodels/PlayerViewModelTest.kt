package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.player.PlayerViewModel
import com.example.workaudio.viewmodels.fakeservice.FakePlayerService
import org.junit.Before
import org.junit.Test

class PlayerViewModelTest {

    private lateinit var playerViewModel: PlayerViewModel

    @Before
    fun setup() {
        playerViewModel = PlayerViewModel(FakePlayerService())
    }


    @Test
    fun test() {

    }


}