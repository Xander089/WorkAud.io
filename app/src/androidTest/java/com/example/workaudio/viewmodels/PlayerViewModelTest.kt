package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.player.PlayerViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakePlayerBoundary
import org.junit.Before
import org.junit.Test

class PlayerViewModelTest {

    private lateinit var playerViewModel: PlayerViewModel

    @Before
    fun setup() {
        playerViewModel = PlayerViewModel(FakePlayerBoundary())
    }


    @Test
    fun test() {

    }


}