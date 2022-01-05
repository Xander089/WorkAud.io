package com.example.workaudio.presentation.player

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.presentation.editing.WorkoutDetailTracksAdapter
import com.example.workaudio.databinding.ActivityPlayerBinding
import com.example.workaudio.core.entities.Track
import com.example.workaudio.dialogs.StopPlayerDialogFragment
import com.example.workaudio.presentation.editing.WorkoutDetailFragment
import com.example.workaudio.spotify.SpotifyManager
import com.google.android.material.slider.RangeSlider
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
    private lateinit var tracksAdapter: WorkoutDetailTracksAdapter
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        spotify = SpotifyManager()
        spotify.spotifyConnect(this)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buildTrackListAdapter()
        setupCurrentWorkout()
        initializeLayout()
        initializeViewModelObservers()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        showStopPlayerDialogFragment()
    }

    override fun onStart() {
        spotify.spotifyConnect(this)
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        spotify.apply {
            pausePlayer()
            spotifyDisconnect()
            logOut(this@PlayerActivity)
        }
    }

    private fun buildTrackListAdapter() {
        tracksAdapter = WorkoutDetailTracksAdapter(
            mutableListOf<Track>(),
            fetchImage = { imageView, imageUrl ->
                Glide.with(this).load(imageUrl).into(imageView)
            }
        )
    }

    private fun setupCurrentWorkout() {
        intent?.extras?.getInt(WORKOUT_ID)?.let { workoutId ->
            viewModel.initializeCurrentWorkout(workoutId)
        }
    }

    private fun initializeLayout() {
        binding.apply {

            timerText.text = resources?.getString(R.string.reset_time).orEmpty()
            playButton.setOnClickListener {
                val playerState = viewModel.getPlayerState()
                if (playerState == PlayerState.PLAYING) {
                    playButton.setBackgroundResource(R.drawable.ic_play)
                    viewModel.setPlayerState(PlayerState.PAUSED)
                    viewModel.stopTimer()
                    spotify.pausePlayer()
                } else {
                    playButton.setBackgroundResource(R.drawable.ic_pause)
                    viewModel.setPlayerState(PlayerState.PLAYING)
                    viewModel.restartTimer(timerText.text.toString())
                }
            }
            stopButton.setOnClickListener {
                //DIALOG
                showStopPlayerDialogFragment()
            }
            trackList.apply {
                layoutManager = LinearLayoutManager(this@PlayerActivity)
                adapter = tracksAdapter
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
                tracksAdapter.tracks.apply {
                    clear()
                    addAll(trackList)
                }
                tracksAdapter.notifyDataSetChanged()
                binding.timerText.text = viewModel.initTimer(trackList.toList())
                startTimer()
            })

            currentTrackPlaying.observe(this@PlayerActivity, { newSongPosition ->
                val state = viewModel.getPlayerState()
                if (state != PlayerState.PAUSED) {
                    val trackUri = getTrackUri(newSongPosition.position)
                    spotify.play(trackUri)
                }

            })


        }

    }

    private fun showStopPlayerDialogFragment() {
        StopPlayerDialogFragment {
            finish()
        }
            .show(supportFragmentManager, StopPlayerDialogFragment.TAG)
    }

}