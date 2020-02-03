package com.kamaltatyana.redgallery.repository

import android.app.Application
import android.provider.Settings.System.getString
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kamaltatyana.redgallery.AppExecutors
import com.kamaltatyana.redgallery.R
import com.kamaltatyana.redgallery.api.ApiResponse
import com.kamaltatyana.redgallery.api.ApiSuccessResponse
import com.kamaltatyana.redgallery.api.PixabayService
import com.kamaltatyana.redgallery.vo.Image
import com.kamaltatyana.redgallery.vo.ListImage
import com.kamaltatyana.redgallery.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val pixabayService: PixabayService,
        private val contextApp: Application
){
        fun search(query: String): LiveData<Resource<List<ListImage>>> {
                return object : NetworkBoundResource<List<ListImage>, ListImage>(appExecutors) {

                        override fun createCall(): LiveData<ApiResponse<ListImage>> {
                                return pixabayService.searchImage(
                                        contextApp.resources.getString(R.string.PIXABAY_API_KEY),
                                        query
                                )
                        }

                        override fun processResponse(response: ApiSuccessResponse<ListImage>): ListImage {
                                return super.processResponse(response)
                        }
                       /* override fun processResponse(response: ApiSuccessResponse<ListImage>) {
                                val body = response.body
                                return body
                        }*/


                        override fun shouldFetch(data: List<ListImage>?): Boolean = data == null
                        override fun loadFromDb(item: ListImage): LiveData<List<ListImage>> {
                                val live = MutableLiveData<List<ListImage>>()
                                val list = mutableListOf<ListImage>()
                                list.add(0, item)
                                live.value = list
                                return live
                        }

                }.asLiveData()
        }
}