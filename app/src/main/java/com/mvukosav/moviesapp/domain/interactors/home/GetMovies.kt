package com.mvukosav.moviesapp.domain.interactors.home

import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class GetMovies @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(): Response<MutableList<MoviesByCategories>?> = moviesRepository.getAllMovies()
}