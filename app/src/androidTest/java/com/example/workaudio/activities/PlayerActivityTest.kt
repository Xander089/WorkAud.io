package com.example.workaudio.activities

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.workaudio.Constants
import com.example.workaudio.presentation.player.PlayerActivity
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class PlayerActivityTest {

    private lateinit var activityScenario : ActivityScenario<PlayerActivity>

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun activityIsLaunched() {
        val intent = Intent(ApplicationProvider.getApplicationContext(),PlayerActivity::class.java)
        intent.putExtra(Constants.WORKOUT_ID, 4)
        activityScenario = ActivityScenario.launch<PlayerActivity>(intent)
        activityScenario.moveToState(Lifecycle.State.STARTED)
        activityScenario.onActivity {
//            assert(it.getCurrentWorkoutId() == 4)
        }

    }
}