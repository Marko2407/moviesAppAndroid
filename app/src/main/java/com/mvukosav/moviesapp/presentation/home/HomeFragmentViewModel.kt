package com.mvukosav.moviesapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.home.UpdateWatchList
import com.mvukosav.moviesapp.domain.interactors.home.GetMovieById
import com.mvukosav.moviesapp.domain.interactors.home.GetMovies
import com.mvukosav.moviesapp.domain.interactors.home.GetRecommendedMovies
import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.domain.models.RecommendedMovies
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val getMovies: GetMovies,
    private val getRecommendedMovies: GetRecommendedMovies,
    private val getMovieById: GetMovieById,
    private val updateWatchList: UpdateWatchList,
) : ViewModel() {

    private val errorLiveData = MutableLiveData<String?>()
    val fetchErrorLiveData: LiveData<String?> = errorLiveData

    private val moviesLiveData = MutableLiveData<MutableList<MoviesByCategories>?>()
    val fetchMoviesLiveData: LiveData<MutableList<MoviesByCategories>?> = moviesLiveData

    private val recommendedMoviesLiveData = MutableLiveData<MutableList<RecommendedMovies>?>()
    val fetchRecommendedMoviesLiveData: LiveData<MutableList<RecommendedMovies>?> =
        recommendedMoviesLiveData


    private val movieByIdLiveData = MutableLiveData<Movie?>()
    val fetchMovieByIdLiveData: LiveData<Movie?> = movieByIdLiveData

    private val updatedMovieLiveData = MutableLiveData<Actions?>()
    val fetchUpdatedMovieLiveData: LiveData<Actions?> = updatedMovieLiveData

    init {
        initDataFetch()
    }

    fun initDataFetch() {
        fetchRecommendedMovies()
        getAllMovies()
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = getMovies()) {
                    is Response.Result -> {
                        moviesLiveData.postValue(response.result)
                    }
                    is Response.Error -> {
                        moviesLiveData.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        moviesLiveData.postValue(null)
                    }
                    is Response.NetworkError -> {
                        moviesLiveData.postValue(null)
                    }
                }
            }
        }
    }

    private fun fetchRecommendedMovies() {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = getRecommendedMovies()) {
                    is Response.Result -> {
                        recommendedMoviesLiveData.postValue(response.result)
                    }
                    is Response.Error -> {
                        recommendedMoviesLiveData.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        recommendedMoviesLiveData.postValue(null)
                    }
                    is Response.NetworkError -> {
                        recommendedMoviesLiveData.postValue(null)
                    }
                }
            }
        }
    }

    fun fetchMovieById(movieId: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = getMovieById(movieId)) {
                    is Response.Result -> {
                        movieByIdLiveData.postValue(response.result)
                    }
                    is Response.Error -> {
                        movieByIdLiveData.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        movieByIdLiveData.postValue(null)
                    }
                    is Response.NetworkError -> {
                        movieByIdLiveData.postValue(null)
                    }
                }
            }
        }
    }

    fun addMovieToWatchList(movieId: String, action: Actions) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = updateWatchList(movieId, action)) {
                    is Response.Result -> {
                        updatedMovieLiveData.postValue(response.result)
                        movieByIdLiveData.value?.isAddedToWatchList =
                            !movieByIdLiveData.value?.isAddedToWatchList!!
                    }
                    is Response.Error -> {
                        updatedMovieLiveData.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        updatedMovieLiveData.postValue(null)
                    }
                    is Response.NetworkError -> {
                        updatedMovieLiveData.postValue(null)
                    }
                }
            }
        }
    }
}
