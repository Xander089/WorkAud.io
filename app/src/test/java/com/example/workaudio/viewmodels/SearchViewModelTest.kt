package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.searchTracks.SearchTracksFragmentViewModel
import com.example.workaudio.viewmodels.fakeservice.FakeSearchService
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private lateinit var searchTracksFragmentViewModel: SearchTracksFragmentViewModel

    @Before
    fun setup() {
        searchTracksFragmentViewModel = SearchTracksFragmentViewModel(FakeSearchService())
    }


    @Test
    fun test() {

    }


}