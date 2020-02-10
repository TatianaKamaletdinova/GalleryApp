package com.kamaltatyana.redgallery.api

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {

    @GET("api/")
    fun searchImage(
        @Query("key") key: String,
        @Query("q") query: String
      //  @Query("colors") colors: String
        //@Query("page") page: Int
    ): LiveData<ApiResponse<ImageSearchResponse>>

    @GET("api/")
    fun searchImage(
        @Query("key") key: String,
        @Query("q") query: String,
        // @Query("colors") colors: String,
        @Query("page") page: Int
    ): Call<ApiResponse<ImageSearchResponse>>
}