package com.mvukosav.moviesapp.presentation.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.databinding.LayoutMoviesByCategoriesBinding
import com.mvukosav.moviesapp.domain.models.RecommendedMovies
import com.mvukosav.moviesapp.utils.SimpleHorizontalItemDecoration

class RecommendedMoviesRecyclerViewAdapter(
    context: Context,
    private val onMovieClickListener: RecommendedMoviesRowRecyclerViewAdapter.OnRecommendedMovieClickListener
) : RecyclerView.Adapter<RecommendedMoviesViewHolder>() {

    private var recommendedMovies: MutableList<RecommendedMovies> = mutableListOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedMoviesViewHolder {
        val inflatedBinding: LayoutMoviesByCategoriesBinding =
            LayoutMoviesByCategoriesBinding.inflate(layoutInflater, parent, false)
        return RecommendedMoviesViewHolder(inflatedBinding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: RecommendedMoviesViewHolder, position: Int) {
        holder.bind(recommendedMovies[position])
    }

    override fun getItemCount(): Int {
        return recommendedMovies.size
    }

    fun swapList(movies: MutableList<RecommendedMovies>) {
        val diffCallBack = RecommendedMoviesDiffCallback(this.recommendedMovies, movies)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.recommendedMovies.clear()
        this.recommendedMovies.addAll(movies)
        diffResult.dispatchUpdatesTo(this)
    }

}

class RecommendedMoviesViewHolder(
    private val binding: LayoutMoviesByCategoriesBinding,
    private val onMovieClickListener: RecommendedMoviesRowRecyclerViewAdapter.OnRecommendedMovieClickListener
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

    fun bind(model: RecommendedMovies) {
        binding.categoryName.text = model.typeRecommendation
        binding.recyclerViewRowMovies.adapter =
            RecommendedMoviesRowRecyclerViewAdapter(context, model.movies, onMovieClickListener)
    }

    companion object {
        private const val SPACE = 10
        private const val START = 0
    }
}

class RecommendedMoviesDiffCallback(
    private val oldItem: MutableList<RecommendedMovies>,
    private val newItem: MutableList<RecommendedMovies>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem[oldItemPosition] == newItem[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItem == newItem
}
