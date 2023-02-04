package com.mvukosav.moviesapp.domain.mappers

import com.mvukosav.moviesapp.GetAllMoviesQuery
import com.mvukosav.moviesapp.GetMovieByIdQuery
import com.mvukosav.moviesapp.GetRecommendedMoviesQuery
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.MoviesBySearchInputQuery
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.domain.models.RecommendedMovies

interface MoviesDataToDomainMapper {

    fun recommendedMoviesDataToDomain(movies: List<GetRecommendedMoviesQuery.MoviesRecommendation>): MutableList<RecommendedMovies>

    fun moviesDataToDomain(movies: MutableList<Movie>): MutableList<MoviesByCategories>

    fun moviesListDataToDomain(movies: List<GetAllMoviesQuery.Movie>): MutableList<Movie>

    fun searchMoviesListDataToDomain(movies: List<MoviesBySearchInputQuery.MoviesBySearchInput>): MutableList<Movie>

    fun movieDataToDomain(movies: GetMovieByIdQuery.MovieById): Movie

    fun updateWatchList(movies: MutableList<Movie>, movieId: String): MutableList<Movie>

    fun getMoviesByCategory(movies: MutableList<Movie>, category: String): MutableList<Movie>

    fun categories(): List<String>

    fun setUserWatchList(favoriteMovies: List<GetUserQuery.FavoriteMovie?>?): MutableList<String>

    fun getUserWatchList(movies: MutableList<Movie>): MutableList<Movie>
}