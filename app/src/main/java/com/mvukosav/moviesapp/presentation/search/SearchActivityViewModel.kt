package com.mvukosav.moviesapp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvukosav.moviesapp.domain.interactors.search.GetSearchMovies
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchActivityViewModel @Inject constructor(
    private val getSearchMovies: GetSearchMovies
) : ViewModel() {
    private val searchMoviesLiveData = MutableLiveData<MutableList<Movie>?>()
    val fetchSearchMoviesLiveData: LiveData<MutableList<Movie>?> = searchMoviesLiveData

    fun fetchSearchResult(searchInput: String) {
        viewModelScope.launch {
            withContext(viewModelScope.coroutineContext) {
                when (val response = getSearchMovies(searchInput)) {
                    is Response.Result -> {
                        searchMoviesLiveData.postValue(response.result)
                    }
                    is Response.Error -> {
                        searchMoviesLiveData.postValue(null)
                    }
                    is Response.ErrorApi -> {
                        searchMoviesLiveData.postValue(null)
                    }
                    is Response.NetworkError -> {
                        searchMoviesLiveData.postValue(null)
                    }
                }
            }
        }
    }
}