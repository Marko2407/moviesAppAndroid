package com.mvukosav.moviesapp.domain.repositories

import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.network.Response

interface MoviesRepository {

    suspend fun getAllMovies(): Response<MutableList<MoviesByCategories>?>

    suspend fun getMovieById(movieId: String): Response<Movie?>

    suspend fun updateWatchList(movieId: String, action: Actions): Response<Actions?>

    fun getMoviesByCategory(category: String): MutableList<Movie>

    fun getWatchList(): MutableList<Movie>

    suspend fun removeFromWatchList(movieId: String):  Response<MutableList<Movie>>
}