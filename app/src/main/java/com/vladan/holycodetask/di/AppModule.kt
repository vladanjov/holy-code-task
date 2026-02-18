package com.vladan.holycodetask.di

import com.vladan.holycodetask.core.common.FusedLocationProvider
import com.vladan.holycodetask.core.common.LocationProvider
import com.vladan.holycodetask.core.network.ConnectivityNetworkMonitor
import com.vladan.holycodetask.core.network.NetworkMonitor
import com.vladan.holycodetask.feature.details.data.repository.DetailsRepositoryImpl
import com.vladan.holycodetask.feature.details.domain.repository.DetailsRepository
import com.vladan.holycodetask.feature.search.data.repository.SearchRepositoryImpl
import com.vladan.holycodetask.feature.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(impl: ConnectivityNetworkMonitor): NetworkMonitor

    @Binds
    @Singleton
    abstract fun bindLocationProvider(impl: FusedLocationProvider): LocationProvider

    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindDetailsRepository(impl: DetailsRepositoryImpl): DetailsRepository
}
