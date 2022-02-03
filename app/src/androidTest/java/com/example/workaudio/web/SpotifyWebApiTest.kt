package com.example.workaudio.web

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.TestDatabaseFactory
import com.example.workaudio.TestRetrofitFactory
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.web.SpotifyWebService
import com.example.workaudio.data.web.Total
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SpotifyWebApiTest {

    private lateinit var webService: SpotifyWebService
    private lateinit var dao: ApplicationDAO


    @Before
    fun createDb() {
        dao = TestDatabaseFactory.createDao()!!
        webService = TestRetrofitFactory.createWebService()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        TestDatabaseFactory.disposeDb()
    }

    @Test
    fun whenTracksSearchedFromWebApi_thenTheyAreReturned() = runBlocking(Dispatchers.Main) {
        //Given
        val queryText = "R"
        val token = dao.getToken()?.token.orEmpty()
        //When
        webService.fetchTracksAsObservable(token, queryText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Total> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Total) {
                    //Then
                    Assert.assertTrue(t.tracks.items.size > 10)
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

}