package com.kamaltatyana.redgallery.di

import android.app.Application
import com.kamaltatyana.redgallery.api.PixabayService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGithubService(): PixabayService {
        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                //.addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(PixabayService::class.java)
    }
}