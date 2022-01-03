package com.example.workaudio.presentation.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemWorkoutListBinding
import com.example.workaudio.core.entities.Workout


class WorkoutAdapter(
    private val _workouts: MutableList<Workout>,
    private val openWorkoutDetail: (id: Int) -> Unit,
    private val showBottomModal: (Int) -> Unit,
    private val fetchImage: (ImageView, String) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.PlaylistViewHolder>() {


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
            workout.duration.also { it ->
                "${it / 60000} min".let { converted ->
                    binding.durationPlaylistText.text = converted
                }
            }

            binding.apply {

                fetchImage(imageView, workout.imageUrl)
                root.apply {
                    setOnLongClickListener {
                        showBottomModal(workout.id)
                        true
                    }
                    setOnClickListener {
                        openWorkoutDetail(workout.id)
                    }
                }
                playlistNameText.text = workout.name

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