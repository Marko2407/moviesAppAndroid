package com.mvukosav.moviesapp.di

import com.mvukosav.moviesapp.data.repositories.RemoteAuthRepository
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideRemoteAuthRepository(
    ): AuthRepository =
        RemoteAuthRepository()


}