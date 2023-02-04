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

class RecommendedMoviesRowRecyclerViewAdapter(
    context: Context,
    private val model: MutableList<Movie>,
    private val onMovieClickListener: OnRecommendedMovieClickListener
) : RecyclerView.Adapter<RecommendedMoviesRowRecyclerViewAdapter.RecommendedMoviesViewHolderRow>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedMoviesViewHolderRow {
        val inflatedView: LayoutItemMovieBinding =
            LayoutItemMovieBinding.inflate(layoutInflater, parent, false)
        return RecommendedMoviesViewHolderRow(inflatedView, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: RecommendedMoviesViewHolderRow, position: Int) {
        holder.bind(model[position])
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class RecommendedMoviesViewHolderRow(
        private val binding: LayoutItemMovieBinding,
        private val onClickListener: OnRecommendedMovieClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = itemView.context

        fun bind(model: Movie) {
            setImage(context, model.img, binding.imgMovie)
            binding.cardMovie.setOnClickListener {
                onClickListener.onRecommendedMovieClicked(model.movieId)
            }
        }
    }


    interface OnRecommendedMovieClickListener {
        fun onRecommendedMovieClicked(movieId: String)
    }
}