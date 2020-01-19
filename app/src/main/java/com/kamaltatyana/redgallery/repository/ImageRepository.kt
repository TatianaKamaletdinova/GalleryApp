package com.kamaltatyana.redgallery.repository

import com.kamaltatyana.redgallery.api.PixabayService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
        private val pixabayService: PixabayService
){
}