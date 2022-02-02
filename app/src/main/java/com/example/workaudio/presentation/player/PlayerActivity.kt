package com.example.workaudio.presentation.player

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.workaudio.common.Constants.STOP_TAG
import com.example.workaudio.common.Constants.WORKOUT_ID
import com.example.workaudio.R
import com.example.workaudio.databinding.ActivityPlayerBinding
import com.example.workaudio.libraries.spotify.SpotifyManager
import com.example.workaudio.presentation.utils.OnScrollListenerFactory
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
        addOnScrollListener()
        viewModel.emitWorkout(getWorkoutIdExtra())
        setupLayout()
        setupObservers()

    }

    override fun onBackPressed() {
        pause()
        showStopPlayerDialog()
    }

    override fun onStart() {
        spotify.spotifyConnect(this, getSpotifyClientId())
        super.onStart()
    }


    override fun onStop() {
        super.onStop()
        shutDownSpotify()
        viewModel.stopTimer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
               onBackPressed()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupLayout() {
        setupPlayButton()
        setupTrackList()
    }

    private fun setupObservers() {
        setSupportActionBar(binding.topAppBar)
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

    private fun addOnScrollListener() {
        binding.trackList.addOnScrollListener(
            OnScrollListenerFactory.create(
                onScrollStateChanged = { newState -> viewModel.scrollState = newState },
                onScrolled = { dy -> onScrolled(dy) }
            ))
    }

    private fun onScrolled(dy: Int) {
        if (dy > 0 && (viewModel.scrollState in listOf(0, 2))) {
            binding.songCardView.visibility = View.GONE
        } else {
            binding.songCardView.visibility = View.VISIBLE
        }
    }

    private fun pause() {
        binding.playButton.setImageResource(R.drawable.ic_play)
        viewModel.setPlayerState(PlayerState.PAUSED)
        viewModel.stopTimer()
        spotify.pausePlayer()
    }

    private fun play() {
        binding.playButton.setImageResource(R.drawable.ic_pause)
        viewModel.setPlayerState(PlayerState.PLAYING)
        viewModel.restartTimer(
            binding.timerText.text.toString(),
            binding.currentTimeText.text.toString(),
            binding.songTotTimeText.text.toString()
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

    private fun setupTrackList() {
        binding.trackList.apply {
            layoutManager =
                LinearLayoutManager(this@PlayerActivity)
            adapter = tracksAdapter
        }
    }

    private fun setupWorkoutObserver() {
        viewModel.workout.observe(this@PlayerActivity, { workout ->
            viewModel.initTimer()
            workout?.let {
                binding.topAppBar.visibility = View.VISIBLE
                binding.topAppBar.title = it.name.uppercase()
                binding.timerText.text = viewModel.formatTimer(it.tracks.toList())
                tracksAdapter.refreshTrackList(it.tracks)
            }
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
            binding.apply {
                currentTimeText.text = songCurrentTime
                trackProgressBar.progress = viewModel.getProgress(songCurrentTime)
            }
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
                setSongImage(songPosition)
                viewModel.setSongTimer(songPosition)
            }
        })
    }

    private fun setSongImage(songPosition: Int) {
        val imageUrl = viewModel.getTrackImageUrl(songPosition)
        Glide.with(this).load(imageUrl).into(
            binding.songImageView
        )
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

    private fun getSpotifyClientId(): String {
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        return ai.metaData["clientId"].toString()
    }

}