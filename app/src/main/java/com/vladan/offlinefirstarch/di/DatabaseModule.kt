package com.vladan.offlinefirstarch.di

import android.content.Context
import androidx.room.Room
import com.vladan.offlinefirstarch.core.database.OfflineFirstArchDatabase
import com.vladan.offlinefirstarch.core.database.dao.SearchResultDao
import com.vladan.offlinefirstarch.core.database.dao.VenueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OfflineFirstArchDatabase =
        Room.databaseBuilder(
                context,
                OfflineFirstArchDatabase::class.java,
                "offlinefirstarch.db",
            ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideVenueDao(database: OfflineFirstArchDatabase): VenueDao = database.venueDao()

    @Provides
    fun provideSearchResultDao(database: OfflineFirstArchDatabase): SearchResultDao = database.searchResultDao()
}
