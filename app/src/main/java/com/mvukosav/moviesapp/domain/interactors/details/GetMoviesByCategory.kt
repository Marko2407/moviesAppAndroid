package com.mvukosav.moviesapp.domain.interactors.details

import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetMoviesByCategory @Inject constructor(private val moviesRepository: MoviesRepository) {

    operator fun invoke (category: String): MutableList<Movie> =
        moviesRepository.getMoviesByCategory(category)
}