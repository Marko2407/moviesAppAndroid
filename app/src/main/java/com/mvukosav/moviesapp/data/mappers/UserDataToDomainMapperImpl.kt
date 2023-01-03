package com.mvukosav.moviesapp.data.mappers

import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.User

class UserDataToDomainMapperImpl : UserDataToDomainMapper {
    override fun userDataToDomain(user: GetUserQuery.UserInfo): User {
        return User(
            userFullName = user.fullName,
            userId = user._id,
            email = user.email,
            refreshToken = user.refreshToken,
            favoriteMovies = favoriteMoviesDataToDomain(user.favoriteMovies)
        )
    }

    private fun favoriteMoviesDataToDomain(favoriteMovie: List<GetUserQuery.FavoriteMovie?>?): MutableList<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        if (favoriteMovie.isNullOrEmpty()) {
            return mutableListOf()
        }
        favoriteMovie.map {
            if (it != null) {
                movies.add(Movie(movieId = it._id))
            }
        }
        return movies
    }
}