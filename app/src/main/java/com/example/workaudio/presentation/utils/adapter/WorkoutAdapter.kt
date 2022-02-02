package com.example.workaudio.presentation.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.common.Constants.MIN
import com.example.workaudio.common.DataHelper
import com.example.workaudio.databinding.ItemWorkoutListBinding
import com.example.workaudio.core.entities.Workout


class WorkoutAdapter(
    private val _workouts: MutableList<Workout>,
    private val openWorkoutDetail: (Int) -> Unit,
    private val showBottomModal: (Int) -> Unit,
    private val fetchImage: (ImageView, String) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.PlaylistViewHolder>(), BaseAdapter {


    fun updateWorkouts(workouts: List<Workout>) {
        _workouts.apply {
            clear()
            addAll(workouts)
            notifyDataSetChanged()
        }
    }

    class PlaylistViewHolder(
        private val binding: ItemWorkoutListBinding,
        private val fetchImage: (ImageView, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            workout: Workout,
            openWorkoutDetail: (id: Int) -> Unit,
            showBottomModal: (id: Int) -> Unit,
        ) {
            val id = workout.id
            val duration = DataHelper.durationToMinutes(workout.duration)

            binding.apply {

                fetchImage(imageView, workout.imageUrl)
                settingsButton.setOnClickListener {
                    showBottomModal(id)
                    true
                }

                openWorkoutDetail.let { open ->
                    imageView.setOnClickListener { open(id) }
                    durationPlaylistText.setOnClickListener { open(id) }
                    playlistNameText.setOnClickListener { open(id) }
                }

                playlistNameText.text = workout.name.uppercase()
                durationPlaylistText.text = "$duration$MIN"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutListBinding.inflate(layoutInflater, parent, false)
        return PlaylistViewHolder(binding, fetchImage)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = _workouts[position]
        holder.bind(playlist, openWorkoutDetail, showBottomModal)
    }

    override fun getItemCount(): Int = _workouts.size
}