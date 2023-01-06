package com.mvukosav.moviesapp.domain.interactors.home

import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class GetMovieById @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movieId: String): Response<Movie?> =
        moviesRepository.getMovieById(movieId)
}