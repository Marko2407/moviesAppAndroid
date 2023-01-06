package com.mvukosav.moviesapp.presentation.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.databinding.LayoutMoviesByCategoriesBinding
import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.utils.SimpleHorizontalItemDecoration

class MoviesRecyclerViewAdapter(
    context: Context,
    private val onMovieClickListener: MoviesRowRecyclerViewAdapter.OnMovieClickListener
) : RecyclerView.Adapter<MoviesViewHolder>() {

    private var moviesByCategories: MutableList<MoviesByCategories> = mutableListOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflatedBinding: LayoutMoviesByCategoriesBinding =
            LayoutMoviesByCategoriesBinding.inflate(layoutInflater, parent, false)
        return MoviesViewHolder(inflatedBinding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(moviesByCategories[position])
    }

    override fun getItemCount(): Int {
        return moviesByCategories.size
    }

    fun swapList(movies: MutableList<MoviesByCategories>) {
        val diffCallBack = MoviesDiffCallback(this.moviesByCategories, movies)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.moviesByCategories.clear()
        this.moviesByCategories.addAll(movies)
        diffResult.dispatchUpdatesTo(this)
    }

}

class MoviesViewHolder(
    private val binding: LayoutMoviesByCategoriesBinding,
    private val onMovieClickListener: MoviesRowRecyclerViewAdapter.OnMovieClickListener
) : RecyclerView.ViewHolder(binding.root) {
    private val simpleHorizontalItemDecoration =
        SimpleHorizontalItemDecoration(binding.root.context, SPACE, START)

    init {
        binding.recyclerViewRowMovies.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(simpleHorizontalItemDecoration)
        }
    }

    val context: Context = itemView.context

    fun bind(model: MoviesByCategories) {
        binding.categoryName.text = model.category.replaceFirstChar { it.titlecase() }
        binding.recyclerViewRowMovies.adapter =
            MoviesRowRecyclerViewAdapter(context, model.listOfMovies, onMovieClickListener)
    }

    companion object {
        private const val SPACE = 10
        private const val START = 0
    }
}

class MoviesDiffCallback(
    private val oldItem: MutableList<MoviesByCategories>,
    private val newItem: MutableList<MoviesByCategories>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem[oldItemPosition] == newItem[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem == newItem
}
