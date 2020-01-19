package com.kamaltatyana.redgallery.api

import retrofit2.http.GET

interface PixabayService {
    @GET("api/")
    fun searchImage()
}