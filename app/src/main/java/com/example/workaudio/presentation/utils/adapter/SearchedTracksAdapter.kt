package com.example.workaudio.presentation.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemTrackBinding
import com.example.workaudio.core.entities.Track


class SearchedTracksAdapter(
    val tracks: MutableList<Track> = mutableListOf(),
    private val addTrack: (Track) -> Unit = {},
    private val fetchImage: (ImageView, String) -> Unit

) : RecyclerView.Adapter<SearchedTracksAdapter.TrackListViewHolder>() , BaseAdapter{

    companion object {

        fun newInstance(
            addTrack: (Track) -> Unit = {},
            fetchImage: (ImageView, String) -> Unit
        ) = SearchedTracksAdapter(addTrack = addTrack, fetchImage = fetchImage)
    }


    inner class TrackListViewHolder(
        private val binding: ItemTrackBinding,
        private val addTrack: (Track) -> Unit,
        private val fetchImage: (ImageView, String) -> Unit

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.apply {
                artistNameText.text = track.artist
                trackNameText.text = track.title
                durationTrackText.text = formatTrackTime(track.duration)
                fetchImage(trackImage, track.imageUrl)
                addButton.apply {
                    setOnClickListener {
                        addTrack(track)

                    }
                }
            }
        }

        private fun formatTrackTime(duration: Int): String {
            val seconds = duration / 1000
            val minutes = seconds / 60
            val remainderSeconds = seconds % 60

            return "$minutes m $remainderSeconds s"
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrackBinding.inflate(layoutInflater, parent, false)
        return TrackListViewHolder(binding, this.addTrack, this.fetchImage)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        tracks[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun refreshTrackList(searchedTracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(searchedTracks)
        notifyDataSetChanged()
    }

    fun removeAddedTrack(track: Track) {
        tracks.remove(track)
        notifyDataSetChanged()
    }
}