package com.example.pagination.presentation

import android.arch.lifecycle.ViewModelProvider
import com.example.pagination.presentation.common.di.ViewModelFactory
import com.example.pagination.presentation.photos.PhotosActivity
import com.example.pagination.presentation.photos.PhotosActivityModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface PresentationModule {
    @ContributesAndroidInjector(modules = [PhotosActivityModule::class])
    fun photosActivityInjector(): PhotosActivity

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}