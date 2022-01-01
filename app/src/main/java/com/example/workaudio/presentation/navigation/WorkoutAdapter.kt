package com.example.workaudio.presentation.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemWorkoutListBinding
import com.example.workaudio.core.entities.Workout


class WorkoutAdapter(
    private val _workouts: MutableList<Workout>,
    private val startWorkout: (id: Int) -> Unit,
    private val deleteWorkout: (id: Int) -> Unit,
    private val bottomModalLambda: () -> Unit,
    private val fetchImage: (ImageView, String) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.PlaylistViewHolder>() {

    fun onItemDismissed(position: Int) {
        if (position in 0..itemCount) {
            deleteWorkout(_workouts[position].id)
        }
    }

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
            workoutEntity: Workout,
            startWorkout: (id: Int) -> Unit,
            deleteWorkout: (id: Int) -> Unit,
            bottomModalLambda: () -> Unit
        ) {
            workoutEntity.duration.also { it ->
                "${it / 60000} min".let { converted ->
                    binding.durationPlaylistText.text = converted
                }
            }

            binding.apply {
                if (workoutEntity.tracks.isNotEmpty()) {
                    fetchImage(imageView, workoutEntity.tracks[0].imageUrl)
                }
                settingsButton.setOnClickListener { bottomModalLambda() }
                playlistNameText.text = workoutEntity.name
                openDetailButton.setOnClickListener {
                    val myId = workoutEntity.id
                    startWorkout(myId)
                }
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
        holder.bind(playlist, startWorkout, deleteWorkout, bottomModalLambda)
    }

    override fun getItemCount(): Int = _workouts.size
}