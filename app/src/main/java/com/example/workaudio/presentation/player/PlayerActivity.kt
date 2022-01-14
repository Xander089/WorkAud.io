package com.example.workaudio.presentation.player

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.presentation.editing.DetailTracksAdapter
import com.example.workaudio.databinding.ActivityPlayerBinding
import com.example.workaudio.core.entities.Track
import com.example.workaudio.presentation.dialogs.StopPlayerDialogFragment
import com.example.workaudio.libraries.spotify.SpotifyManager
import com.example.workaudio.Constants.STOP_TAG
import com.example.workaudio.Constants.WORKOUT_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, workoutId: Int): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(WORKOUT_ID, workoutId)
            return intent
        }
    }

    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var spotify: SpotifyManager
    private lateinit var tracksAdapter: PlayerTracksAdapter
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
        spotify.pausePlayer()
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
        tracksAdapter = PlayerTracksAdapter(
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
                    spotify.resumePlayer()
                }
            }
            stopButton.setOnClickListener {
                viewModel.stopTimer()
                spotify.pausePlayer()
                showStopPlayerDialogFragment()
            }
            trackList.apply {
                layoutManager =
                    LinearLayoutManager(this@PlayerActivity, LinearLayoutManager.HORIZONTAL, false)
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
                tracksAdapter.refreshTrackList(trackList)
                binding.timerText.text = viewModel.initTimer(trackList.toList())
                startTimer()
            })

            currentTrackPlaying.observe(this@PlayerActivity, { newSongPosition ->
                val state = viewModel.getPlayerState()
                if (state != PlayerState.PAUSED) {
                    tracksAdapter.refreshPlayingTrack(newSongPosition.position)
                    val trackUri = getTrackUri(newSongPosition.position)
                    spotify.play(trackUri)
                    binding.trackProgressBar.progress = 0
                    binding.currentTimeText.text = getString(R.string.reset_song_time)
                    binding.songTotTimeText.text = formatTrackDuration(newSongPosition.position)
                }

            })


        }

    }

    private fun showStopPlayerDialogFragment() {
        StopPlayerDialogFragment {
            spotify.spotifyDisconnect()
            finish()
        }.show(
            supportFragmentManager,
            STOP_TAG
        )
    }

}