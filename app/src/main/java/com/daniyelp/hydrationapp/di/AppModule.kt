package com.daniyelp.hydrationapp.di

import android.content.Context
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import com.daniyelp.hydrationapp.data.repository.impl.FakeDayProgressRepository
import com.daniyelp.hydrationapp.data.repository.impl.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context) = PreferencesRepository(context)

    @Provides
    @Singleton
    fun provideDayProgressRepository(): DayProgressRepository = FakeDayProgressRepository()
}