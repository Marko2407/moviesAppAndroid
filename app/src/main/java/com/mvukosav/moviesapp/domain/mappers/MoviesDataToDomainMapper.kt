package com.mvukosav.moviesapp.domain.mappers

import com.mvukosav.moviesapp.GetAllMoviesQuery
import com.mvukosav.moviesapp.GetMovieByIdQuery
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.MoviesByCategories

interface MoviesDataToDomainMapper {

    fun moviesDataToDomain(movies: MutableList<Movie>): MutableList<MoviesByCategories>

    fun moviesListDataToDomain(movies: List<GetAllMoviesQuery.Movie>): MutableList<Movie>

    fun movieDataToDomain(movies: GetMovieByIdQuery.MovieById): Movie

    fun updateWatchList(movies: MutableList<Movie>, movieId: String): MutableList<Movie>

    fun categories(): List<String>

    fun setUserWatchList(favoriteMovies: List<GetUserQuery.FavoriteMovie?>?): MutableList<String>

    fun getUserWatchList(movies: MutableList<Movie>): MutableList<Movie>
}