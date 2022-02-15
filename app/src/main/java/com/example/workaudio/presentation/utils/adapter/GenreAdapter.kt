package com.example.workaudio.presentation.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workaudio.databinding.ItemGenreBinding


class GenreAdapter(
    private val genres: MutableList<String>,
    private val action: (String) -> Unit,
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>(), BaseAdapter {

    fun update(g: List<String>) {
        genres.apply {
            clear()
            addAll(g)
            notifyDataSetChanged()
        }
    }

    class GenreViewHolder(
        private val binding: ItemGenreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            genre: String,
            action: (String) -> Unit
        ) {
            binding.apply {
                genreTextView.text = genre
                root.setOnClickListener {
                    action(genre)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreBinding.inflate(layoutInflater, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.bind(genre) {
            action(it)
        }
    }

    override fun getItemCount(): Int = genres.size
}