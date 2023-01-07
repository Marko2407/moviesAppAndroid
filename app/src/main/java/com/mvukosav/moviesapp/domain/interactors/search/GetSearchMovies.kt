package com.mvukosav.moviesapp.domain.interactors.search

import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetSearchMovies @Inject constructor(
   private val moviesRepository: MoviesRepository
){
    suspend operator fun invoke(searchInput: String) = moviesRepository.getSearchMovies(searchInput)
}