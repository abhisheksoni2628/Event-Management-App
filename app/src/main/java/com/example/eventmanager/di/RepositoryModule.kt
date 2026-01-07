package com.example.eventmanager.di

import com.example.eventmanager.data.remote.EventApi
import com.example.eventmanager.data.repository.EventRepository
import com.example.eventmanager.data.repository.EventRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideEventRepository(apiService: EventApi): EventRepository =
        EventRepositoryImpl(apiService)
}