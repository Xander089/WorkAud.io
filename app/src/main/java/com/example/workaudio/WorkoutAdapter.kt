package com.example.workaudio

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemWorkoutListBinding
import com.example.workaudio.entities.Workout


class WorkoutAdapter(
    private val workoutEntities: MutableList<Workout>,
    private val startWorkout: (id: Int) -> Unit,
    private val deleteWorkout: (id: Int) -> Unit,
    private val bottomModalLambda: () -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.PlaylistViewHolder>() {

    fun onItemDismissed(position: Int) {
        if (position in 0..itemCount) {
            deleteWorkout(workoutEntities[position].id)
        }
    }

    fun updateWorkouts(workouts: List<Workout>) {
        workoutEntities.apply {
            clear()
            addAll(workouts)
            notifyDataSetChanged()
        }
    }

    class PlaylistViewHolder(
        private val binding: ItemWorkoutListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            workoutEntity: Workout,
            startWorkout: (id: Int) -> Unit,
            deleteWorkout: (id: Int) -> Unit,
            bottomModalLambda: () -> Unit
        ) {
            workoutEntity.duration?.also { it ->
                "${it / 60000} min".let { converted ->
                    binding.durationPlaylistText.text = converted
                }
            }
            binding.settingsButton.setOnClickListener { bottomModalLambda() }
            binding.playlistNameText.text = workoutEntity.name
            binding.openDetailButton.setOnClickListener {
                val myId = workoutEntity.id
                startWorkout(myId)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutListBinding.inflate(layoutInflater, parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = workoutEntities[position]
        holder.bind(playlist, startWorkout, deleteWorkout, bottomModalLambda)
    }

    override fun getItemCount(): Int = workoutEntities.size
}