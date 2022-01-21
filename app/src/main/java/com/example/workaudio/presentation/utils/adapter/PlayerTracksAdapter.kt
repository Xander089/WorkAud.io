package com.example.workaudio.presentation.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.core.entities.Track
import com.example.workaudio.databinding.ItemDetailTrackHorizontalBinding


class PlayerTracksAdapter(
    val tracks: MutableList<Track> = mutableListOf(),
    private val fetchImage: (ImageView, String) -> Unit
) : RecyclerView.Adapter<PlayerTracksAdapter.TrackListViewHolder>(), BaseAdapter {

    inner class TrackListViewHolder(
        private val binding: ItemDetailTrackHorizontalBinding,
        private val fetchImage: (ImageView, String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {

            binding.apply {
                artistNameText.text = track.artist
                trackNameText.text = track.title
                fetchImage(trackImage, track.imageUrl)
            }
//            decoratePlayingSong(track.isPlaying, binding)

        }

    }

//    private fun decoratePlayingSong(
//        isPlaying: Boolean,
//        binding: ItemDetailTrackHorizontalBinding
//    ) {
//        binding.playButton.visibility = if (isPlaying) View.VISIBLE else View.INVISIBLE
//        binding.trackImage.alpha = if (isPlaying) 0.4F else 1.0F
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailTrackHorizontalBinding.inflate(layoutInflater, parent, false)
        return TrackListViewHolder(
            binding,
            this.fetchImage,
        )
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        tracks[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = tracks.size

//    fun refreshTrack(position: Int) {
//        tracks.forEach { it.isPlaying = false }
//        tracks[position].isPlaying = true
//        notifyItemChanged(position)
//    }

    fun refreshTrackList(_tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(_tracks)
        notifyDataSetChanged()
    }

}