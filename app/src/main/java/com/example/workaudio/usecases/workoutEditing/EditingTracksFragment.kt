package com.example.workaudio.usecases.workoutEditing

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.activityViewModels
import com.example.workaudio.R
import com.example.workaudio.databinding.FragmentEditingTracksBinding
import com.example.workaudio.entities.Track


class EditingTracksFragment : Fragment() {

    private val viewModel: WorkoutEditingViewModel by activityViewModels()


    private lateinit var binding: FragmentEditingTracksBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditingTracksBinding.inflate(inflater, container, false)

        arguments?.getInt("id")?.let { workoutId ->
            viewModel.setSelectedWorkout(workoutId)
        }

        binding.apply {
            arrow.setOnClickListener {
                it.setBackgroundResource(R.drawable.ic_up)
                val params = songsCardView.layoutParams
                params.height = Constraints.LayoutParams.WRAP_CONTENT
                songsCardView.layoutParams = params
            }
        }

        viewModel.tracks.observe(this, { tracks ->
            binding.apply {
                songsTextView.text = concatSongs(tracks)
            }
        })

        return binding.root
    }

    private fun concatSongs(tracks: List<Track>): String {
        var concat = ""
        tracks.forEach {
            concat += "${it.title} - ${it.artist}, "
        }
        return concat
    }

}