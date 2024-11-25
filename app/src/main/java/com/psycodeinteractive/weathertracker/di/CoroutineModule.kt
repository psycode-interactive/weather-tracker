package com.psycodeinteractive.weathertracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun providesCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher)
    }
}
