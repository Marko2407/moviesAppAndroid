package com.mvukosav.moviesapp.domain.interactors.home

import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class UpdateWatchList @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movieId: String, actions: Actions): Response<Actions?> =
        moviesRepository.updateWatchList(movieId,actions)
}