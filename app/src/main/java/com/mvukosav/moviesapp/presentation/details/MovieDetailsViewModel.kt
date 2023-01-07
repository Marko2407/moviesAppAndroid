package com.mvukosav.moviesapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.details.GetMoviesByCategory
import com.mvukosav.moviesapp.domain.interactors.home.GetMovieById
import com.mvukosav.moviesapp.domain.interactors.home.UpdateWatchList
import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovie: GetMovieById,
    private val getMoviesByCategory: GetMoviesByCategory,
    private val updateWatchList: UpdateWatchList
) : ViewModel() {

    private val errorLiveData = MutableLiveData<String?>()
    val fetchErrorLiveData: LiveData<String?> = errorLiveData


    private val movieLiveData = MutableLiveData<Movie?>()
    val fetchMoviesLiveData: LiveData<Movie?> = movieLiveData


    private val moviesByCategory = MutableLiveData<MutableList<Movie>?>()
    val fetchMoviesByCategoryListMoviesLiveData: LiveData<MutableList<Movie>?> = moviesByCategory

    private val updatedWatchList = MutableLiveData<Actions?>()
    val fetchUpdatedWatchListMoviesLiveData: LiveData<Actions?> = updatedWatchList


    fun fetchMovieById(movieId: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = getMovie(movieId)) {
                    is Response.Result -> {
                        movieLiveData.postValue(response.result)
                        getMoviesListByCategory(response.result?.category?.get(0) ?: "")
                    }
                    is Response.Error -> {
                        movieLiveData.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        movieLiveData.postValue(null)
                    }
                    is Response.NetworkError -> {
                        movieLiveData.postValue(null)
                    }
                }
            }
        }
    }

    private fun getMoviesListByCategory(category: String){
        val result = getMoviesByCategory(category)
        if (result.isEmpty()){
            moviesByCategory.postValue(null)
        }else{
            moviesByCategory.postValue(result)
        }
    }

    fun updateMovieToWatchList(movieId: String, action: Actions){
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = updateWatchList(movieId, action)) {
                    is Response.Result -> {
                        updatedWatchList.postValue(response.result)
                        movieLiveData.value?.isAddedToWatchList =  !movieLiveData.value?.isAddedToWatchList!!
                    }
                    is Response.Error -> {
                        updatedWatchList.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        updatedWatchList.postValue(null)
                    }
                    is Response.NetworkError -> {
                        updatedWatchList.postValue(null)
                    }
                }
            }
        }
    }
}