package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.creation.DurationFragmentViewModel
import com.example.workaudio.viewmodels.fakeservice.FakeCreationService
import org.junit.Before
import org.junit.Test


class CreationViewModelTest {

    private lateinit var viewModel: DurationFragmentViewModel

    @Before
    fun setup() {
        viewModel = DurationFragmentViewModel(FakeCreationService())
    }


    @Test
    fun test() {

    }


}