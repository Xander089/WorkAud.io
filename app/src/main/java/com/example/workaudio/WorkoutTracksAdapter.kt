package com.example.workaudio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemTrackBinding
import com.example.workaudio.entities.Track
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity

import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

class WorkoutTracksAdapter(
    val tracks: MutableList<Track>,
    private val addTrack: (Track) -> Unit
) : RecyclerView.Adapter<WorkoutTracksAdapter.TrackListViewHolder>() {

    inner class TrackListViewHolder(
        private val binding: ItemTrackBinding,
        private val addTrack: (Track) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.apply {
                artistNameText.text = track.artist
                trackNameText.text = track.title
                durationTrackText.text = formatTrackTime(track.duration)
                fetchImage(track.imageUrl, binding)
                addButton.apply {
                    setOnClickListener {
                        addTrack(track)
                        isEnabled = false
                    }
                }
            }
        }

        private fun formatTrackTime(duration: Int): String{
            val seconds = duration/1000
            val minutes = seconds/60
            val remainderSeconds = seconds%60

            return "${minutes} m ${remainderSeconds} s"
        }

        @DelicateCoroutinesApi
        private fun fetchImage(image: String, binding: ItemTrackBinding) {

            val urlImage = URL(image)
            val result: Deferred<Bitmap?> = GlobalScope.async {
                try {
                    BitmapFactory.decodeStream(urlImage.openStream())
                } catch (e: IOException) {
                    null
                }
            }

            GlobalScope.launch(Dispatchers.Main) {
                val bitmap: Bitmap? = result.await()
                if (bitmap != null) {
                    binding.trackImage.setImageBitmap(bitmap)
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrackBinding.inflate(layoutInflater, parent, false)
        return TrackListViewHolder(binding, this.addTrack)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        tracks[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = tracks.size
}