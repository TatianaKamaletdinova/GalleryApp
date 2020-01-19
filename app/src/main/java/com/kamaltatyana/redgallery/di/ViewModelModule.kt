package com.kamaltatyana.redgallery.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kamaltatyana.redgallery.view.search.SearchViewModel
import com.kamaltatyana.redgallery.viewmodel.GalleryViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: GalleryViewModelFactory): ViewModelProvider.Factory
}