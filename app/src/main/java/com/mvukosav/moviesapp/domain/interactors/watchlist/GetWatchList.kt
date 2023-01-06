package com.mvukosav.moviesapp.domain.interactors.watchlist

import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetWatchList @Inject constructor(
    private val moviesRepository: MoviesRepository
){
    operator fun invoke(): MutableList<Movie> = moviesRepository.getWatchList()

}