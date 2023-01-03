package com.mvukosav.moviesapp.di

import android.content.Context
import com.mvukosav.moviesapp.data.mappers.UserDataToDomainMapperImpl
import com.mvukosav.moviesapp.data.repositories.RemoteAuthRepository
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
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
    fun provideUserMapper(): UserDataToDomainMapper = UserDataToDomainMapperImpl()

    @Singleton
    @Provides
    fun provideRemoteAuthRepository(
        provideUserDataToDomainMapper: UserDataToDomainMapper,
        @ApplicationContext context: Context
    ): AuthRepository =
        RemoteAuthRepository(provideUserDataToDomainMapper, context)


}