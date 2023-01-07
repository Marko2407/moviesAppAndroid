package com.mvukosav.moviesapp.presentation.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.LayoutItemMovieBinding
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.utils.setImage

class MoviesRowRecyclerViewAdapter(
    context: Context,
    private val model: MutableList<Movie>,
    private val onMovieClickListener: OnMovieClickListener
) : RecyclerView.Adapter<MoviesRowRecyclerViewAdapter.MoviesViewHolderRow>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolderRow {
        val inflatedView: LayoutItemMovieBinding =
            LayoutItemMovieBinding.inflate(layoutInflater, parent, false)
        return MoviesViewHolderRow(inflatedView, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MoviesViewHolderRow, position: Int) {
        holder.bind(model[position])
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class MoviesViewHolderRow(
        private val binding: LayoutItemMovieBinding,
        private val onClickListener: OnMovieClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = itemView.context

        fun bind(model: Movie) {
            setImage(context, model.img, binding.imgMovie)
            binding.cardMovie.setOnClickListener {
                onClickListener.onMovieClicked(model.movieId)
            }
        }
    }


    interface OnMovieClickListener {
        fun onMovieClicked(movieId: String)
    }
}