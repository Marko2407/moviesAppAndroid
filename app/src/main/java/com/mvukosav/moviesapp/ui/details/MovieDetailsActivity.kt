package com.mvukosav.moviesapp.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivityMovieDetailsBinding
import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.presentation.details.MovieDetailsViewModel
import com.mvukosav.moviesapp.presentation.details.adapters.MoviesByCategoryRecyclerViewAdapter
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRowRecyclerViewAdapter
import com.mvukosav.moviesapp.utils.SimpleHorizontalItemDecoration
import com.mvukosav.moviesapp.utils.TimeUtil
import com.mvukosav.moviesapp.utils.setVisible
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity(),
    MoviesRowRecyclerViewAdapter.OnMovieClickListener {

    private lateinit var binding: ActivityMovieDetailsBinding
    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var moviesByCategoryRecyclerViewAdapter: MoviesByCategoryRecyclerViewAdapter
    private var mYouTubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.txtToolbarTitle.text = this.getString(R.string.details)
        val movieId = intent.getStringExtra(MOVIE_ID)
        setOnCLickListeners()
        initRecyclerViews()
        observeMovie()
        observeMoviesByCategory()
        observeUpdatedWatchList()
        fetchMovie(movieId)
    }

    private fun setOnCLickListeners() {
        binding.toolbar.btnToolbarStart.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerViews() {
        val simpleHorizontalItemDecoration =
            SimpleHorizontalItemDecoration(binding.root.context, SPACE, START)
        binding.recyclerViewMoviesList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(simpleHorizontalItemDecoration)
            moviesByCategoryRecyclerViewAdapter =
                MoviesByCategoryRecyclerViewAdapter(context, this@MovieDetailsActivity)
            adapter = moviesByCategoryRecyclerViewAdapter
        }
    }

    private fun fetchMovie(movieId: String?) {
        if (movieId != null) viewModel.fetchMovieById(movieId)
        else Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show()
    }

    private fun observeMovie() {
        viewModel.fetchMoviesLiveData.observe(this) {
            if (it != null) {
                renderMoviesDetails(it)
                initializePlayer(it.url, it.title)
            } else {
                Toast.makeText(this, "Unable to fetch movie", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeMoviesByCategory() {
        viewModel.fetchMoviesByCategoryListMoviesLiveData.observe(this) {
            if (it != null) {
                moviesByCategoryRecyclerViewAdapter.swapList(it)
            } else {
                Toast.makeText(this, "Unable to fetch movie", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeUpdatedWatchList() {
        viewModel.fetchUpdatedWatchListMoviesLiveData.observe(this) {
            binding.toolbar.btnToolbarEnd.background = setWatchListIconColor(Actions.ADD == it)
            if (it == Actions.ADD) Toast.makeText(
                this,
                "Successfully added item",
                Toast.LENGTH_SHORT
            ).show()
            else Toast.makeText(this, "Successfully removed item", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePlayer(movieUrl: String?, movieTitle: String) {
        if (movieUrl != null) {
            binding.videoMovieTrailer.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    mYouTubePlayer = youTubePlayer
                    youTubePlayer.loadOrCueVideo(lifecycle, movieUrl, 0f)
                }
            })
        } else {
            Toast.makeText(this, "Unable to find trailer", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mYouTubePlayer?.pause()
    }

    private fun renderMoviesDetails(movie: Movie) {
        binding.toolbar.btnToolbarEnd.background = setWatchListIconColor(movie.isAddedToWatchList)
        binding.toolbar.btnToolbarEnd.setVisible()
        binding.toolbar.btnToolbarEnd.setOnClickListener {
            viewModel.updateMovieToWatchList(
                movie.movieId,
                if (movie.isAddedToWatchList) Actions.REMOVE else Actions.ADD
            )
        }

        binding.movieTitle.text = movie.title
        binding.layoutCategory.apply {
            txtTitle.text = movie.category[0].lowercase().replaceFirstChar { it.titlecase() }
            imgIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@MovieDetailsActivity,
                    R.drawable.category_ic
                )
            )
        }
        binding.layoutDuration.apply {
            txtTitle.text = this@MovieDetailsActivity.getString(R.string._120_min, movie.duration)
            imgIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@MovieDetailsActivity,
                    R.drawable.clock_ic
                )
            )
        }
        binding.layoutYear.apply {
            txtTitle.text = TimeUtil.convertLongToTime(movie.releaseDate.toLong())
            imgIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@MovieDetailsActivity,
                    R.drawable.calendar_ic
                )
            )
        }
        binding.layoutRating.apply {
            txtTitle.text = movie.rating.toString()
            imgIcon.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@MovieDetailsActivity,
                    R.drawable.star_ic
                )
            )
        }
        binding.movieDescription.text = movie.description
    }

    private fun setWatchListIconColor(isAddedToWatchList: Boolean): Drawable? {
        return if (isAddedToWatchList) ContextCompat.getDrawable(
            this,
            R.drawable.fav_ic_blue
        ) else {
            ContextCompat.getDrawable(this, R.drawable.fav_ic)
        }

    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MovieDetailsActivity::class.java)
        const val MOVIE_ID = "MovieId"
        private const val SPACE = 20
        private const val START = 0
    }

    override fun onMovieClicked(movieId: String) {
        val i = createIntent(this)
        i.putExtra("MovieId", movieId)
        startActivity(i)
    }
}
