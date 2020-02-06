package com.kamaltatyana.redgallery.api

import androidx.lifecycle.LiveData
import com.kamaltatyana.redgallery.vo.Image
import com.kamaltatyana.redgallery.vo.ListImage
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PixabayService {
    @GET("api/")
    fun searchImage(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("colors") colors: String,
        @Query("page") page: Int
        ): LiveData<ApiResponse<ImageSearchResponse>>
}