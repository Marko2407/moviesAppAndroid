package com.mvukosav.moviesapp.domain.interactors.watchlist

import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class RemoveFromWatchList @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movieId: String):  Response<MutableList<Movie>> =
        moviesRepository.removeFromWatchList(movieId)
}