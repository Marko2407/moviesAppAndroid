package com.mvukosav.moviesapp.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.FragmentHomeBinding
import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.presentation.home.HomeFragmentViewModel
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRecyclerViewAdapter
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRowRecyclerViewAdapter
import com.mvukosav.moviesapp.presentation.home.adapters.RecommendedMoviesRecyclerViewAdapter
import com.mvukosav.moviesapp.presentation.home.adapters.RecommendedMoviesRowRecyclerViewAdapter
import com.mvukosav.moviesapp.ui.details.MovieDetailsActivity
import com.mvukosav.moviesapp.utils.TimeUtil.convertLongToTime
import com.mvukosav.moviesapp.utils.setGone
import com.mvukosav.moviesapp.utils.setImage
import com.mvukosav.moviesapp.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), MoviesRowRecyclerViewAdapter.OnMovieClickListener, RecommendedMoviesRowRecyclerViewAdapter.OnRecommendedMovieClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var recommendedMoviesRecyclerViewAdapter: RecommendedMoviesRecyclerViewAdapter
    private lateinit var moviesRecyclerViewAdapter: MoviesRecyclerViewAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var progressBarDialog: Dialog
    private lateinit var movieAdd: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        initRecyclerView()
        createClickListeners()
        observeRecommendedMovies()
        observeMovies()
        observeMovieDetailsById()
        observeAddedMovies()
    }

    override fun onPause() {
        super.onPause()
        bottomSheetDialog.hide()
    }

    private fun initRecyclerView() {
        binding.includeMoviesContainer.recyclerViewMovies.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            moviesRecyclerViewAdapter = MoviesRecyclerViewAdapter(context, this@HomeFragment)
            adapter = moviesRecyclerViewAdapter
        }

        binding.includeMoviesContainer.recyclerViewRecommendedMovies.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recommendedMoviesRecyclerViewAdapter = RecommendedMoviesRecyclerViewAdapter(context, this@HomeFragment)
            adapter = recommendedMoviesRecyclerViewAdapter
        }
    }

    private fun createClickListeners() {
        binding.btnCategories.setOnClickListener {

        }

        binding.btnFetchMovies.setOnClickListener {
            viewModel.initDataFetch()
        }
    }

    private fun observeRecommendedMovies() {
        viewModel.fetchRecommendedMoviesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                recommendedMoviesRecyclerViewAdapter.swapList(it)
                binding.includeMoviesContainer.root.setVisible()
            } else {
                binding.btnCategories.setGone()
                binding.btnFetchMovies.setVisible()
            }
        }
    }
    private fun observeMovies() {
        viewModel.fetchMoviesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                moviesRecyclerViewAdapter.swapList(it)
            } else {
                binding.btnCategories.setGone()
                binding.btnFetchMovies.setVisible()
            }
        }
    }

    private fun observeMovieDetailsById() {
        viewModel.fetchMovieByIdLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                showMovieDetailBottomSheet(it)
            } else {
                Toast.makeText(context, "Unable to fetch movie details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeAddedMovies() {
        viewModel.fetchUpdatedMovieLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it == Actions.ADD) {
                    Toast.makeText(context, "Added to watch list", Toast.LENGTH_SHORT).show()
                    movieAdd.setImageResource(R.drawable.remove_icon)
                } else {
                    Toast.makeText(context, "Removed from watch list", Toast.LENGTH_SHORT).show()
                    movieAdd.setImageResource(R.drawable.add_icon)
                }
            } else {
                Toast.makeText(context, "Uknown error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeError() {
        viewModel.fetchErrorLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
//                hideProgressBarDialog()
//                toastMsg(requireContext(), requireContext().convertErrorToProperMsg(it))
            }
        }
    }

    private fun showMovieDetailBottomSheet(movie: Movie) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_bottom_sheet_movie_details, binding.root, false)
        bottomSheetDialog.setContentView(view)
        val movieTitle = view.findViewById<TextView>(R.id.txt_movie_title)
        val movieDescription = view.findViewById<TextView>(R.id.txt_movie_description)
        val movieDuration = view.findViewById<TextView>(R.id.txt_movie_duration)
        val movieYear = view.findViewById<TextView>(R.id.txt_movie_year)
        val movieImg = view.findViewById<ImageView>(R.id.img_movie)
        movieAdd = view.findViewById<ImageView>(R.id.btn_add_to_watch_list)
        view.setOnClickListener {
            //open details screen
            val i = MovieDetailsActivity.createIntent(requireContext())
            i.putExtra("MovieId", movie.movieId)
            startActivity(i)
        }

        setImage(requireContext(), movie.img, movieImg)
        movieTitle.text = movie.title
        movieDescription.text = movie.description
        movieDuration.text = requireContext().getString(R.string._120_min, movie.duration)
        movieYear.text = convertLongToTime(movie.releaseDate.toLong())
        if (movie.isAddedToWatchList) movieAdd.setImageResource(R.drawable.remove_icon)
        else movieAdd.setImageResource(R.drawable.add_icon)

        movieAdd.setOnClickListener {
            if (movie.isAddedToWatchList) viewModel.addMovieToWatchList(
                movie.movieId,
                Actions.REMOVE
            )
            else viewModel.addMovieToWatchList(movie.movieId, Actions.ADD)
        }
        bottomSheetDialog.show()
    }

    override fun onMovieClicked(movieId: String) {
        viewModel.fetchMovieById(movieId)
    }

    override fun onRecommendedMovieClicked(movieId: String) {
        viewModel.fetchMovieById(movieId)
    }
}
