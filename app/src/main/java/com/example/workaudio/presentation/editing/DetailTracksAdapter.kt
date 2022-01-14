package com.example.workaudio.presentation.editing

import android.R.attr
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemDetailTrackBinding
import com.example.workaudio.core.entities.Track
import com.example.workaudio.presentation.utils.ItemTouchHelperAdapter
import android.R.attr.data
import java.util.*


class DetailTracksAdapter(
    val tracks: MutableList<Track> = mutableListOf(),
    private val fetchImage: (ImageView, String) -> Unit,
    private val deleteTrack: (String) -> Unit = {},
    val onSwipe : (String) -> Unit = {}
) : RecyclerView.Adapter<DetailTracksAdapter.TrackListViewHolder>(), ItemTouchHelperAdapter {

    inner class TrackListViewHolder(
        private val binding: ItemDetailTrackBinding,
        private val fetchImage: (ImageView, String) -> Unit,
        private val deleteTrack: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {

            binding.apply {
                artistNameText.text = track.artist
                trackNameText.text = track.title
                fetchImage(trackImage, track.imageUrl)
                settingsButton.setOnClickListener {
                    deleteTrack(track.uri)
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailTrackBinding.inflate(layoutInflater, parent, false)
        return TrackListViewHolder(
            binding,
            this.fetchImage,
            this.deleteTrack
        )
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        tracks[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun refreshTrackList(_tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(_tracks)
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {
        onSwipe(tracks[position].uri)
        tracks.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tracks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tracks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}