package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.editing.DetailFragmentViewModel
import com.example.workaudio.viewmodels.fakeservice.FakeEditingService
import org.junit.Before
import org.junit.Test

class EditingViewModelTest {

    private lateinit var viewModel: DetailFragmentViewModel

    @Before
    fun setup() {
        viewModel = DetailFragmentViewModel(FakeEditingService())
    }


    @Test
    fun test() {

    }


}