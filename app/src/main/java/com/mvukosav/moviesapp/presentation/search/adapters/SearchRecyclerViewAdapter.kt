package com.mvukosav.moviesapp.presentation.search.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.LayoutSearchListBinding
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRowRecyclerViewAdapter
import com.mvukosav.moviesapp.utils.TimeUtil
import com.mvukosav.moviesapp.utils.setImage

class SearchRecyclerViewAdapter(
    context: Context,
    private val onMovieClickListener: MoviesRowRecyclerViewAdapter.OnMovieClickListener
) : RecyclerView.Adapter<SearchViewHolder>() {
    private var searchResult: MutableList<Movie> = mutableListOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflatedBinding: LayoutSearchListBinding =
            LayoutSearchListBinding.inflate(layoutInflater, parent, false)
        return SearchViewHolder(inflatedBinding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        return holder.bind(searchResult[position])
    }

    override fun getItemCount(): Int = searchResult.size


    fun swapList(searchResult: MutableList<Movie>) {
        val diffCallBack = SearchMoviesDiffCallback(this.searchResult, searchResult)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.searchResult.clear()
        this.searchResult.addAll(searchResult)
        diffResult.dispatchUpdatesTo(this)
    }
}

class SearchViewHolder(
    private val binding: LayoutSearchListBinding,
    private val onMovieClickListener: MoviesRowRecyclerViewAdapter.OnMovieClickListener
) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = itemView.context
    fun bind(model: Movie) {
        setImage(context, model.img, binding.imgMovie)
        binding.root.setOnClickListener {
            onMovieClickListener.onMovieClicked(model.movieId)
        }
        binding.txtMovieTitle.text = model.title
        binding.txtMovieCategory.txtTitle.text = model.category[0].lowercase().replaceFirstChar { it.titlecase() }
        binding.txtMovieYeary.apply {
            txtTitle.text = TimeUtil.convertLongToTime(model.releaseDate.toLong())
            imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_ic))
        }
        binding.txtMovieDuration.apply {
            txtTitle.text = context.getString(R.string._120_min, model.duration)
            imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.clock_ic))
        }
        binding.txtMovieRating.apply {
            txtTitle.text = model.rating.toString()
            imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_ic))
        }

    }
}

class SearchMoviesDiffCallback(
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
