package com.mvukosav.moviesapp.domain.models

data class RecommendedMovies(val typeRecommendation: String? = "", val movies: MutableList<Movie>)
