package com.mvukosav.moviesapp.di

import android.content.Context
import com.mvukosav.moviesapp.data.mappers.MoviesDataToDomainMapperImpl
import com.mvukosav.moviesapp.data.mappers.UserDataToDomainMapperImpl
import com.mvukosav.moviesapp.data.repositories.RemoteAuthRepository
import com.mvukosav.moviesapp.data.repositories.RemoteMoviesRepository
import com.mvukosav.moviesapp.domain.mappers.MoviesDataToDomainMapper
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.domain.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideUserMapper(moviesDataToDomainMapper: MoviesDataToDomainMapper): UserDataToDomainMapper =
        UserDataToDomainMapperImpl(moviesDataToDomainMapper)

    @Singleton
    @Provides
    fun provideMoviesMapper(@ApplicationContext context: Context): MoviesDataToDomainMapper =
        MoviesDataToDomainMapperImpl(context)

    @Singleton
    @Provides
    fun provideRemoteAuthRepository(
        provideUserDataToDomainMapper: UserDataToDomainMapper,
        @ApplicationContext context: Context
    ): AuthRepository =
        RemoteAuthRepository(provideUserDataToDomainMapper, context)


    @Singleton
    @Provides
    fun provideRemoteMoviesRepository(
        provideMoviesDataToDomainMapper: MoviesDataToDomainMapper,
    ): MoviesRepository =
        RemoteMoviesRepository(provideMoviesDataToDomainMapper)
}