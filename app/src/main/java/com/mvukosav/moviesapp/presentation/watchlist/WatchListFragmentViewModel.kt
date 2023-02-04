package com.mvukosav.moviesapp.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.watchlist.RemoveFromWatchList
import com.mvukosav.moviesapp.domain.interactors.watchlist.GetWatchList
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WatchListFragmentViewModel @Inject constructor(
    private val getWatchList: GetWatchList,
    private val updateWatchList: RemoveFromWatchList,
) : ViewModel() {

    private val errorLiveData = MutableLiveData<String?>()
    val fetchErrorLiveData: LiveData<String?> = errorLiveData

    private val watchListMoviesLiveData = MutableLiveData<MutableList<Movie>?>()
    val fetchWatchListMoviesLiveData: LiveData<MutableList<Movie>?> = watchListMoviesLiveData

    private val updatedMovieLiveData = MutableLiveData<MutableList<Movie>?>()
    val fetchUpdatedMovieLiveData: LiveData<MutableList<Movie>?> = updatedMovieLiveData

    init {
        initDataFetch()
    }

    fun initDataFetch() {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                val response = getWatchList()
                watchListMoviesLiveData.postValue(response)
            }
        }
    }

    fun removeFromWatchList(movieId: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = updateWatchList(movieId)) {
                    is Response.Result -> {
                        updatedMovieLiveData.postValue(response.result)
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