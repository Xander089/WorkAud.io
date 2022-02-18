package com.example.workaudio.presentation.utils.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.res.colorResource
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.common.Constants
import com.example.workaudio.databinding.ItemGenreBinding
import kotlin.random.Random


class GenreAdapter(
    private val genres: MutableList<String>,
    private val action: (String) -> Unit,
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>(), BaseAdapter {

    private var screenWidth: Int = 0

    fun update(g: List<String>, width: Int) {
        genres.apply {
            clear()
            addAll(g)
            notifyDataSetChanged()
        }
        screenWidth = width / 2
    }

    class GenreViewHolder(
        private val binding: ItemGenreBinding,
        private val screenWidth: Int,
        val color: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            genre: String,
            action: (String) -> Unit
        ) {
            binding.apply {
                genreTextView.text = genre
                root.apply {
                    layoutParams.width = screenWidth
                    setOnClickListener {
                        action(genre)
                    }
                }
                cardView.background.setTint(color)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreBinding.inflate(layoutInflater, parent, false)
        return GenreViewHolder(binding, screenWidth, pickColor(parent.context))
    }

    private fun pickColor(context: Context): Int {
        val randInt = Random.nextInt(7)
        val colorId = Constants.colorMap[randInt] ?: 0
        return context.resources.getColor(colorId, null)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.bind(genre) {
            action(it)
        }
    }

    override fun getItemCount(): Int = genres.size
}