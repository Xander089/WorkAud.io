package com.example.workaudio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemDetailTrackBinding
import com.example.workaudio.databinding.ItemTrackBinding
import com.example.workaudio.entities.Track
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity

import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

class WorkoutDetailTracksAdapter(
    val tracks: MutableList<Track>,
    private val addTrack: (Track) -> Unit
) : RecyclerView.Adapter<WorkoutDetailTracksAdapter.TrackListViewHolder>() {

    inner class TrackListViewHolder(
        private val binding: ItemDetailTrackBinding,
        private val addTrack: (Track) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            Log.v("tracce",track.toString())
            binding.apply {
                artistNameText.text = track.artist
                trackNameText.text = track.title
                durationTrackText.text = formatTrackTime(track.duration)
                fetchImage(track.imageUrl, binding)
            }
        }

        private fun formatTrackTime(duration: Int): String{
            val seconds = duration/1000
            val minutes = seconds/60
            val remainderSeconds = seconds%60

            return "${minutes} m ${remainderSeconds} s"
        }

        @DelicateCoroutinesApi
        private fun fetchImage(image: String, binding: ItemDetailTrackBinding) {

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
        val binding = ItemDetailTrackBinding.inflate(layoutInflater, parent, false)
        return TrackListViewHolder(binding, this.addTrack)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        tracks[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = tracks.size
}