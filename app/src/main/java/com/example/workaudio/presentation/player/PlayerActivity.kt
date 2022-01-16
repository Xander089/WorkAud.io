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

    //1. LIFECYCLE CALLBACKS

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        spotify.spotifyConnect(this)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCurrentWorkout()
        initializeLayout()
        initializeViewModelObservers()

    }

    override fun onBackPressed() {
        pause()
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

    //2. Methods invoked inside onCreate
    private fun setupCurrentWorkout() {
        intent?.extras?.getInt(WORKOUT_ID)?.let { workoutId ->
            viewModel.initializeCurrentWorkout(workoutId)
        }
    }

    private fun initializeLayout() {
        setupPlayButton()
        setupStopButton()
        setupTrackListRecyclerView()
    }

    private fun initializeViewModelObservers() {

        setupWorkoutObserver()
        setupTimerObserver()
        setupTrackObserver()
        setupCurrentPlayingSongObserver()
        setupPlayingSongInfoObserver()

    }

    fun timerNotEmpty() = binding.timerText.text.isNotEmpty()
    fun getExtra() = intent?.extras?.getInt(WORKOUT_ID) ?: -1

    private fun buildTrackListAdapter(): PlayerTracksAdapter {
        return AdapterFactory.create(AdapterFlavour.PLAYER,
            fetchImage = { imageView, imageUrl ->
                Glide.with(this).load(imageUrl).into(imageView)
            }
        ) as PlayerTracksAdapter
    }

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
            if (viewModel.getPlayerState() == PlayerState.PLAYING) {
                pause()
            } else {
                play()
            }
        }
    }

    private fun setupStopButton() {
        binding.stopButton.setOnClickListener {
            pause()
            showStopPlayerDialogFragment()
        }
    }

    private fun setupTrackListRecyclerView() {
        binding.trackList.apply {
            layoutManager =
                LinearLayoutManager(this@PlayerActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = tracksAdapter
        }
    }

    private fun setupPlayingSongInfoObserver() {
        viewModel.playingTrackText.observe(this@PlayerActivity, { songCurrentTime ->
            binding.currentTimeText.text = songCurrentTime
            binding.trackProgressBar.progress = viewModel.getProgress(songCurrentTime)
        })
    }

    private fun setupCurrentPlayingSongObserver() {
        viewModel.currentTrackPlaying.observe(this@PlayerActivity, { newSongPosition ->
            if (viewModel.getPlayerState() != PlayerState.PAUSED) {
                tracksAdapter.refreshPlayingTrack(newSongPosition.position)
                spotify.play(viewModel.getTrackUri(newSongPosition.position))
                binding.apply {
                    trackProgressBar.progress = 0
                    currentTimeText.text = getString(R.string.reset_song_time)
                    songTotTimeText.text = viewModel.formatTrackDuration(newSongPosition.position)
                }
                viewModel.resetSongTimer(newSongPosition.position)
            }
        })
    }

    private fun setupTrackObserver() {
        viewModel.tracks.observe(this@PlayerActivity, { trackList ->
            tracksAdapter.refreshTrackList(trackList)
            binding.timerText.text = viewModel.initTimer(trackList.toList())
            viewModel.startTimer()
        })
    }

    private fun setupTimerObserver() {
        viewModel.timerText.observe(this@PlayerActivity, { timerText ->
            binding.timerText.text = timerText
            spotify.stopSpotifyPlayer(timerText)
        })
    }

    private fun setupWorkoutObserver() {
        viewModel.selectedWorkout.observe(this@PlayerActivity, { workout ->
            binding.apply {
                workoutNameText.text = workout.name
            }
            viewModel.initializeWorkoutTracks(workout.tracks)
        })
    }

    private fun showStopPlayerDialogFragment() {
        DialogFactory.create(
            DialogFlavour.STOP_PLAYER,
            ok = {
                spotify.spotifyDisconnect()
                finish()
            },
            cancel = { play() }
        ).show(
            supportFragmentManager,
            STOP_TAG
        )
    }

}