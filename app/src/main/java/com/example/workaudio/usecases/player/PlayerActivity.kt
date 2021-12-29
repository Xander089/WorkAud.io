package com.example.workaudio.usecases.player

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workaudio.R
import com.example.workaudio.WorkoutDetailTracksAdapter
import com.example.workaudio.databinding.ActivityPlayerBinding
import com.example.workaudio.entities.Track
import com.example.workaudio.usecases.login.SpotifyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    companion object {

        private const val MAX_MILLISECONDS = 1800000L
        private const val MILLIS_PER_SECOND = 1000L
        private const val START_TIME = "00:00:15"
        private const val RESET_TIME = "00:00:00"
        private const val WORKOUT_ID = "WORKOUT_ID"

        fun newIntent(context: Context, workoutId: Int): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(WORKOUT_ID, workoutId)
            return intent
        }
    }

    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var _adapter: WorkoutDetailTracksAdapter
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        _adapter = WorkoutDetailTracksAdapter(mutableListOf<Track>(), {})

        intent?.extras?.getInt(WORKOUT_ID)?.let { workoutId ->
            viewModel.setSelectedWorkout(workoutId)
        }

        binding.apply {
            timerText.text = START_TIME
            playButton.setOnClickListener {
                viewModel.tracks.value?.get(0)?.let { it1 -> SpotifyUtils.play(it1.uri) }
            }
            pauseButton.setOnClickListener { }
            stopButton.setOnClickListener {
                timerText.text = START_TIME
            }
            trackList.apply {
                layoutManager = LinearLayoutManager(this@PlayerActivity)
                adapter = _adapter
            }
        }

        viewModel.selectedWorkout.observe(this, { workout ->
            binding.apply {
                workoutNameText.text = workout.name
            }
        })

        viewModel.tracks.observe(this, { trackList ->
            _adapter.tracks.apply {
                clear()
                addAll(trackList)
            }
            _adapter.notifyDataSetChanged()
        })

    }

    override fun onStart() {
        SpotifyUtils.spotifyConnect(this)
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        SpotifyUtils.spotifyDisconnect()
        SpotifyUtils.logOut(this)

    }
}