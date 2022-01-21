package com.example.workaudio.presentation.player

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.R
import com.example.workaudio.databinding.ActivityPlayerBinding
import com.example.workaudio.presentation.utils.dialogs.StopPlayerDialogFragment
import com.example.workaudio.libraries.spotify.SpotifyManager
import com.example.workaudio.Constants.STOP_TAG
import com.example.workaudio.Constants.WORKOUT_ID
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.presentation.utils.adapter.AdapterFactory
import com.example.workaudio.presentation.utils.adapter.AdapterFlavour
import com.example.workaudio.presentation.utils.adapter.PlayerTracksAdapter
import com.example.workaudio.presentation.utils.dialogs.DialogFactory
import com.example.workaudio.presentation.utils.dialogs.DialogFlavour
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    @Inject
    lateinit var spotify: SpotifyManager

    private var tracksAdapter: PlayerTracksAdapter = buildTrackListAdapter()
    private lateinit var binding: ActivityPlayerBinding

    //LIFECYCLE CALLBACKS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.emitWorkout(getWorkoutIdExtra())
        setupLayout()
        setupObservers()

    }

    override fun onBackPressed() {
        pause()
        showStopPlayerDialog()
    }

    override fun onStart() {
        spotify.spotifyConnect(this)
        super.onStart()
    }


    override fun onStop() {
        super.onStop()
        shutDownSpotify()
        viewModel.stopTimer()
    }


    private fun setupLayout() {
        setupPlayButton()
        setupStopButton()
        setupTrackList()
    }

    private fun setupObservers() {

        setupWorkoutObserver()
        setupMainTimerObserver()
        setupPlayerPositionObserver()
        setupPlayingTrackObserver()

    }

    private fun getWorkoutIdExtra() = intent?.extras?.getInt(WORKOUT_ID) ?: -1

    private fun buildTrackListAdapter(): PlayerTracksAdapter =
        AdapterFactory.create(AdapterFlavour.PLAYER,
            fetchImage = { imageView, imageUrl ->
                Glide.with(this).load(imageUrl).into(imageView)
            }
        ) as PlayerTracksAdapter


    private fun pause() {
        binding.playButton.setBackgroundResource(R.drawable.ic_play)
        viewModel.setPlayerState(PlayerState.PAUSED)
        viewModel.stopTimer()
        spotify.pausePlayer()
    }

    private fun play() {
        binding.playButton.setBackgroundResource(R.drawable.ic_pause)
        viewModel.setPlayerState(PlayerState.PLAYING)
        viewModel.restartTimer(
            binding.timerText.text.toString(),
            binding.currentTimeText.text.toString()
        )
        spotify.resumePlayer()
    }


    private fun setupPlayButton() {
        binding.playButton.setOnClickListener {
            when (viewModel.getPlayerState()) {
                PlayerState.PAUSED -> play()
                PlayerState.PLAYING -> pause()
            }
        }
    }

    private fun setupStopButton() {
        binding.stopButton.setOnClickListener {
            pause()
            showStopPlayerDialog()
        }
    }

    private fun setupTrackList() {
        binding.trackList.apply {
            layoutManager =
                LinearLayoutManager(
                    this@PlayerActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = tracksAdapter
        }
    }

    private fun setupWorkoutObserver() {
        viewModel.workout.observe(this@PlayerActivity, { workout ->
            binding.workoutNameText.text = workout.name
            binding.timerText.text = viewModel.formatTimer(workout.tracks.toList())
            tracksAdapter.refreshTrackList(workout.tracks)
            viewModel.initTimer()

        })
    }


    private fun setupMainTimerObserver() {
        viewModel.timerText.observe(this@PlayerActivity, { timerText ->
            binding.timerText.text = timerText
            spotify.stopSpotifyPlayer(timerText)
        })
    }

    private fun setupPlayingTrackObserver() {
        viewModel.songTimerText.observe(this@PlayerActivity, { songCurrentTime ->
            binding.currentTimeText.text = songCurrentTime
            binding.trackProgressBar.progress = viewModel.getProgress(songCurrentTime)
        })
    }

    private fun setupPlayerPositionObserver() {
        viewModel.playerPosition.observe(this@PlayerActivity, { songPosition ->
            if (viewModel.getPlayerState() != PlayerState.PAUSED) {
                spotify.play(viewModel.getTrackUri(songPosition))
                binding.apply {
                    trackProgressBar.progress = 0
                    currentTimeText.text = getString(R.string.reset_song_time)
                    songTotTimeText.text = viewModel.formatTrackDuration(songPosition)
                    songTitleText.text = viewModel.getTrackName(songPosition)
                    songArtistText.text = viewModel.getTrackArtist(songPosition)
                }
                viewModel.setSongTimer(songPosition)
            }
        })
    }


    private fun shutDownSpotify() {
        spotify.apply {
            pausePlayer()
            spotifyDisconnect()
            logOut(this@PlayerActivity)
        }
    }

    private fun showStopPlayerDialog() {
        DialogFactory.create(
            DialogFlavour.STOP_PLAYER,
            ok = {
                shutDownSpotify()
                finish()
            },
            cancel = { play() }
        ).show(
            supportFragmentManager,
            STOP_TAG
        )
    }

}