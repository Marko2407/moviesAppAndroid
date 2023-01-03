package com.mvukosav.moviesapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationContextModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}