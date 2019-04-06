package com.example.pagination.common

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CommonModule {
    @Binds
    @Singleton
    fun bind(impl: SchedulersProviderImpl): SchedulersProvider
}