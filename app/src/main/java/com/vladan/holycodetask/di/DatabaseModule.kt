package com.vladan.holycodetask.di

import android.content.Context
import androidx.room.Room
import com.vladan.holycodetask.core.database.HolyCodeTaskDatabase
import com.vladan.holycodetask.core.database.dao.SearchResultDao
import com.vladan.holycodetask.core.database.dao.VenueDao
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
    fun provideDatabase(@ApplicationContext context: Context): HolyCodeTaskDatabase =
        Room.databaseBuilder(
            context,
            HolyCodeTaskDatabase::class.java,
            "holycodetask.db",
        ).build()

    @Provides
    fun provideVenueDao(database: HolyCodeTaskDatabase): VenueDao = database.venueDao()

    @Provides
    fun provideSearchResultDao(database: HolyCodeTaskDatabase): SearchResultDao = database.searchResultDao()
}
