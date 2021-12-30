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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val WORKOUT_ID = "WORKOUT_ID"
        fun newIntent(context: Context, workoutId: Int): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(WORKOUT_ID, workoutId)
            return intent
        }
    }

    private val viewModel: PlayerViewModel by viewModels()
    private val spotify = SpotifyManager()
    private lateinit var _adapter: WorkoutDetailTracksAdapter
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        spotify.spotifyConnect(this)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        _adapter = WorkoutDetailTracksAdapter(mutableListOf<Track>(), {})

        intent?.extras?.getInt(WORKOUT_ID)?.let { workoutId ->
            viewModel.initializeCurrentWorkout(workoutId)
        }

        initializeLayout()
        initializeViewModelObservers()

    }

    override fun onStart() {
        spotify.spotifyConnect(this)
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        spotify.spotifyDisconnect()
        spotify.logOut(this)

    }

    private fun initializeLayout() {
        binding.apply {

            timerText.text = resources?.getString(R.string.reset_time).orEmpty()
            playButton.setOnClickListener {
                viewModel.startTimer()
            }
            pauseButton.setOnClickListener { }
            stopButton.setOnClickListener {
                timerText.text = resources?.getString(R.string.reset_time).orEmpty()
            }
            trackList.apply {
                layoutManager = LinearLayoutManager(this@PlayerActivity)
                adapter = _adapter
            }
        }
    }

    private fun initializeViewModelObservers() {

        viewModel.apply {

            selectedWorkout.observe(this@PlayerActivity, { workout ->
                binding.apply {
                    workoutNameText.text = workout.name
                }
                viewModel.initializeWorkoutTracks(workout.tracks)
            })

            timerText.observe(this@PlayerActivity, { timerText ->
                binding.timerText.text = timerText
                stopSpotifyPlayer(timerText)
            })

            viewModel.tracks.observe(this@PlayerActivity, { trackList ->
                _adapter.tracks.apply {
                    clear()
                    addAll(trackList)
                }
                _adapter.notifyDataSetChanged()
            })

            currentTrackPosition.observe(this@PlayerActivity, { newSongPosition ->
                val state = viewModel.playerState.value ?: 0
                if (state != 0) {
                    val trackUri = getTrackUri(newSongPosition.position)
                    spotify.play(trackUri)
                }

            })


        }

    }

    private fun stopSpotifyPlayer(timerText: String) {

        val resetTimeText = resources?.getString(R.string.reset_time).orEmpty()
        if (timerText == resetTimeText) {
            spotify.mSpotifyAppRemote?.playerApi?.pause()
        }
    }


}