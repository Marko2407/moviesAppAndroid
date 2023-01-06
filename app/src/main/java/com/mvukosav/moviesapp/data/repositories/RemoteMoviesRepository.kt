package com.mvukosav.moviesapp.data.repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.mvukosav.moviesapp.AddToFavoriteMutation
import com.mvukosav.moviesapp.GetAllMoviesQuery
import com.mvukosav.moviesapp.GetMovieByIdQuery
import com.mvukosav.moviesapp.RemoveFromFavoriteMutation
import com.mvukosav.moviesapp.domain.mappers.MoviesDataToDomainMapper
import com.mvukosav.moviesapp.domain.models.Actions
import com.mvukosav.moviesapp.domain.models.Movie
import com.mvukosav.moviesapp.domain.models.MoviesByCategories
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import com.mvukosav.moviesapp.network.Response
import com.mvukosav.moviesapp.network.graphql.GraphQlManager
import com.mvukosav.moviesapp.utils.ErrorManager.errorHandler
import com.mvukosav.moviesapp.utils.ErrorManager.isErrorApi
import com.mvukosav.moviesapp.utils.ErrorManager.parserApiError
import javax.inject.Inject

class RemoteMoviesRepository @Inject constructor(
    private val moviesDataToDomainMapper: MoviesDataToDomainMapper
) : MoviesRepository {
    private var movies: MutableList<Movie> = mutableListOf()

    override suspend fun getAllMovies(): Response<MutableList<MoviesByCategories>?> {
        return try {
            val response = GraphQlManager.apolloClient().query(GetAllMoviesQuery()).execute()
            val movies =
                response.data?.movies

            when {
                isErrorApi(response.errors) -> parserApiError(response.errors)
                else -> {
                    if (movies != null) {
                        this.movies = moviesDataToDomainMapper.moviesListDataToDomain(movies)
                        val mappedMovies = moviesDataToDomainMapper.moviesDataToDomain(this.movies)
                        Response.Result(mappedMovies)
                    } else Response.Result(null)
                }
            }
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    override suspend fun getMovieById(movieId: String): Response<Movie?> {
        return try {
            val response = GraphQlManager.apolloClient().query(GetMovieByIdQuery(movieId)).execute()
            val movies =
                response.data?.movieById
            when {
                isErrorApi(response.errors) -> parserApiError(response.errors)
                else -> {
                    if (movies != null) {
                        val mappedMovies = moviesDataToDomainMapper.movieDataToDomain(movies)
                        Response.Result(mappedMovies)
                    } else Response.Result(null)
                }
            }
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    override suspend fun updateWatchList(movieId: String, action: Actions): Response<Actions?> {
        return try {
            val response =
                if (action == Actions.ADD) addToWatchListMutation(movieId) else removeFromWatchListMutation(
                    movieId
                )
            when {
                isErrorApi(response.errors) -> parserApiError(response.errors)
                else -> {
                    return run {
                        val mappedMovies =
                            moviesDataToDomainMapper.updateWatchList(movies, movieId)
                        this.movies = mappedMovies
                        Response.Result(action)
                    }
                }
            }
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    override fun getMoviesByCategory(category: String): MutableList<Movie> {
        //Map movies by category
        return mutableListOf()
    }

    override fun getWatchList(): MutableList<Movie> {
        return moviesDataToDomainMapper.getUserWatchList(movies)
    }

    override suspend fun removeFromWatchList(movieId: String): Response<MutableList<Movie>> {
        return try {
            val response = removeFromWatchListMutation(movieId)
            when {
                isErrorApi(response.errors) -> parserApiError(response.errors)
                else -> {
                    val mappedMovies = moviesDataToDomainMapper.updateWatchList(movies, movieId)
                    this.movies = mappedMovies
                    return Response.Result(moviesDataToDomainMapper.getUserWatchList(movies))
                }
            }
        } catch (e: Exception) {
            return errorHandler(e)
        }
    }

    private suspend fun addToWatchListMutation(movieId: String): ApolloResponse<AddToFavoriteMutation.Data> {
        return GraphQlManager.apolloClient()
            .mutation(AddToFavoriteMutation(movieId, "63b4a592d59cfb4cc8dbe8fe")).execute()
    }

    private suspend fun removeFromWatchListMutation(movieId: String): ApolloResponse<RemoveFromFavoriteMutation.Data> {
        return GraphQlManager.apolloClient()
            .mutation(RemoveFromFavoriteMutation(movieId, "63b4a592d59cfb4cc8dbe8fe")).execute()
    }
}