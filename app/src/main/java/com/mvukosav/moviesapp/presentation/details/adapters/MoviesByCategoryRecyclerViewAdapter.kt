package com.mvukosav.moviesapp.presentation.details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.LayoutItemMovieBinding
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRowRecyclerViewAdapter
import com.mvukosav.moviesapp.utils.setImage

class MoviesByCategoryRecyclerViewAdapter(
    context: Context,
    private val onMovieClickListener: MoviesRowRecyclerViewAdapter.OnMovieClickListener
) : RecyclerView.Adapter<MovieByCategoryViewHolder>() {
    private val movies = mutableListOf<Movie>()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieByCategoryViewHolder {
        val inflatedView: LayoutItemMovieBinding =
            LayoutItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieByCategoryViewHolder(inflatedView, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MovieByCategoryViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun swapList(movies: MutableList<Movie>) {
        val diffCallBack = MoviesByCategoryDiffCallback(this.movies, movies)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.movies.clear()
        this.movies.addAll(movies)
        diffResult.dispatchUpdatesTo(this)
    }
}

class MovieByCategoryViewHolder(
    private val binding: LayoutItemMovieBinding,
    private val onMovieClickListener: MoviesRowRecyclerViewAdapter.OnMovieClickListener
) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = itemView.context

    fun bind(model: Movie) {
        setImage(context, model.img, binding.imgMovie)
        binding.cardMovie.setOnClickListener {
            onMovieClickListener.onMovieClicked(model.movieId)
        }
    }
}


class MoviesByCategoryDiffCallback(
    private val oldItem: MutableList<Movie>,
    private val newItem: MutableList<Movie>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem[oldItemPosition] == newItem[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem == newItem
}
