package com.example.pagination.presentation.photos

import android.arch.lifecycle.ViewModel
import com.example.pagination.presentation.common.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PhotosActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(PhotosViewModel::class)
    fun bindPhotosViewModel(viewModel: PhotosViewModel): ViewModel
}