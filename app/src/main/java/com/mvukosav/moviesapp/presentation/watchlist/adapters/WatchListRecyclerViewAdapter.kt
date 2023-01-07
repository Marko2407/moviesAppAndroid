package com.mvukosav.moviesapp.presentation.watchlist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.LayoutWatchListBinding
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.utils.TimeUtil.convertLongToTime
import com.mvukosav.moviesapp.utils.setImage

class WatchListRecyclerViewAdapter(
    context: Context,
    private val onWatchListClickListener: OnWatchListClickListener
) : RecyclerView.Adapter<WatchListHolder>() {
    private var watchList: MutableList<Movie> = mutableListOf()
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListHolder {
        val inflatedBinding: LayoutWatchListBinding =
            LayoutWatchListBinding.inflate(layoutInflater, parent, false)
        return WatchListHolder(inflatedBinding, onWatchListClickListener)
    }

    override fun onBindViewHolder(holder: WatchListHolder, position: Int) {
        holder.bind(watchList[position])
    }

    override fun getItemCount(): Int = watchList.size

    fun swapList(movies: MutableList<Movie>) {
        val diffCallBack = WatchListDiffCallback(this.watchList, movies)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.watchList.clear()
        this.watchList.addAll(movies)
        diffResult.dispatchUpdatesTo(this)
    }
}

class WatchListHolder(
    private val binding: LayoutWatchListBinding,
    private val onWatchListClickListener: OnWatchListClickListener
) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = itemView.context
    fun bind(model: Movie) {
        binding.cancelButton.apply {
            this.horizontalBottomDivider.setBackgroundColor(context.getColor(R.color.red))
            this.horizontalUpperDivider.setBackgroundColor(context.getColor(R.color.red))
            this.logout.text = context.getString(R.string.remove)
        }
        binding.detailsButton.apply {
            this.horizontalBottomDivider.setBackgroundColor(context.getColor(R.color.baby_blue))
            this.horizontalUpperDivider.setBackgroundColor(context.getColor(R.color.baby_blue))
            this.logout.text = context.getString(R.string.details)
        }
        setImage(context, model.img, binding.imgMovieWatchList)
        binding.txtMovieCategory.txtTitle.text =
            model.category[0].lowercase().replaceFirstChar { it.titlecase() }
        binding.txtMovieTitle.text = model.title
        binding.txtMovieYeary.apply {
            txtTitle.text = convertLongToTime(model.releaseDate.toLong())
            imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_ic))
        }
        binding.txtMovieDuration.apply {
            txtTitle.text = context.getString(R.string._120_min, model.duration)
            imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.clock_ic))
        }

        binding.cancelButton.root.setOnClickListener {
            onWatchListClickListener.onRemoveClicked(model.movieId)
        }
        binding.detailsButton.root.setOnClickListener {
            onWatchListClickListener.onDetailsClicked(model.movieId)
        }

        binding.cardMovie.setOnClickListener {
            onWatchListClickListener.onDetailsClicked(model.movieId)
        }

    }
}


class WatchListDiffCallback(
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

interface OnWatchListClickListener {
    fun onDetailsClicked(movieId: String)
    fun onRemoveClicked(movieId: String)
}


