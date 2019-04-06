package com.example.pagination

import android.content.Context
import com.example.pagination.common.CommonModule
import com.example.pagination.data.DataModule
import com.example.pagination.domain.DomainModule
import com.example.pagination.presentation.PresentationModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        CommonModule::class,
        DataModule::class,
        DomainModule::class,
        PresentationModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @dagger.Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>() {
        @BindsInstance
        internal abstract fun appContext(context: Context): Builder
    }

}