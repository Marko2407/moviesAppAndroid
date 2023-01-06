package com.mvukosav.moviesapp.data.mappers

import android.content.Context
import com.mvukosav.moviesapp.GetAllMoviesQuery
import com.mvukosav.moviesapp.GetMovieByIdQuery
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.mappers.MoviesDataToDomainMapper
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.type.Category

class MoviesDataToDomainMapperImpl(private val context: Context) : MoviesDataToDomainMapper {
    private val categories: MutableList<String> = mutableListOf()
    private val userWatchList: MutableList<String> = mutableListOf()

    override fun moviesDataToDomain(movies: MutableList<Movie>): MutableList<MoviesByCategories> {
        val moviesByCategories = mutableListOf<MoviesByCategories>()
        categories.forEach { category ->
            val filteredMovies = movies.filter {
                it.category.contains(category)
            }.toMutableList()
            moviesByCategories.add(MoviesByCategories(category.lowercase(), filteredMovies))
        }

        return moviesByCategories
    }

    override fun moviesListDataToDomain(movies: List<GetAllMoviesQuery.Movie>): MutableList<Movie> {
        return movies.map {
            movieDataToDomainMapper(it)
        }.toMutableList()
    }

    override fun movieDataToDomain(movie: GetMovieByIdQuery.MovieById): Movie {
        return Movie(
            movie._id,
            movie.title,
            movie.description,
            categoriesDataToDomainMapper(movie.category),
            movie.duration,
            movie.releaseDate,
            movie.url,
            movie.img,
            isAddedToWatchList = userWatchList.contains(movie._id)
        )
    }

    override fun updateWatchList(
        movies: MutableList<Movie>,
        movieId: String
    ): MutableList<Movie> {
        movies.find {
            it.movieId == movieId
        }?.let {
            if (it.isAddedToWatchList) {
                userWatchList.remove(it.movieId)
                it.isAddedToWatchList = false
            } else {
                userWatchList.add(it.movieId)
                it.isAddedToWatchList = true
            }
        }
        return movies
    }

    override fun categories(): List<String> = categories

    override fun setUserWatchList(favoriteMovies: List<GetUserQuery.FavoriteMovie?>?): MutableList<String> {
        if (favoriteMovies.isNullOrEmpty()) return mutableListOf()
        favoriteMovies.map {
            if (it != null) {
                userWatchList.add(it._id)
            }
        }
        return userWatchList
    }

    override fun getUserWatchList(movies: MutableList<Movie>): MutableList<Movie> {
        movies.removeIf{ movie->
            !userWatchList.contains(movie.movieId)
        }
         return movies
    }

    private fun getAllCategories(category: List<Category?>?) {
        category?.forEach { it ->
            if (it != null) {
                if (!categories.contains(it.name)) {
                    categories.add(it.name)
                }
            }

        }
    }

    private fun movieDataToDomainMapper(movies: GetAllMoviesQuery.Movie): Movie {
        getAllCategories(movies.category)
        return Movie(
            movieId = movies._id,
            title = movies.title,
            description = movies.description,
            category = categoriesDataToDomainMapper(movies.category),
            duration = movies.duration,
            releaseDate = movies.releaseDate,
            img = movies.img,
            url = movies.url,
            isAddedToWatchList = userWatchList.contains(movies._id)
        )
    }


    private fun categoriesDataToDomainMapper(category: List<Category?>?): List<String> {
        val categoriesList = mutableListOf<String>()
        category?.forEach {
            if (it != null) {
                categoriesList.add(it.name)
            }
        }
        return categoriesList
    }
}