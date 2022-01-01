package com.example.workaudio.presentation.player

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.presentation.editing.WorkoutDetailTracksAdapter
import com.example.workaudio.databinding.ActivityPlayerBinding
import com.example.workaudio.core.entities.Track
import com.example.workaudio.spotify.SpotifyManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    private lateinit var spotify: SpotifyManager

    private lateinit var _adapter: WorkoutDetailTracksAdapter
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        spotify = SpotifyManager()
        spotify.spotifyConnect(this)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        _adapter = WorkoutDetailTracksAdapter(mutableListOf<Track>()) { imageView, imageUrl ->
            Glide.with(this).load(imageUrl).into(imageView)
        }

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
        spotify.apply {
            spotifyDisconnect()
            logOut(this@PlayerActivity)
        }
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
                spotify.stopSpotifyPlayer(timerText)
            })

            tracks.observe(this@PlayerActivity, { trackList ->
                _adapter.tracks.apply {
                    clear()
                    addAll(trackList)
                }
                _adapter.notifyDataSetChanged()
            })

            currentTrackPlaying.observe(this@PlayerActivity, { newSongPosition ->
                val state = viewModel.playerState.value ?: 0
                if (state != 0) {
                    val trackUri = getTrackUri(newSongPosition.position)
                    spotify.play(trackUri)
                }

            })


        }

    }




}