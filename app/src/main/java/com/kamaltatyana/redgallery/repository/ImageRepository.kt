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
import com.kamaltatyana.redgallery.api.ImageSearchResponse
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

      /*  fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
                val fetchNextSearchPageTask = FetchNextSearchPageTask(
                        query = query,
                        pixabayService = pixabayService
                )
        }*/


        fun search(query: String): LiveData<Resource<List<Image>>> {
                return object : NetworkBoundResource<List<Image>, ImageSearchResponse>(appExecutors) {

                        override fun createCall(): LiveData<ApiResponse<ImageSearchResponse>> {
                                val p = ""
                                return pixabayService.searchImage(
                                        contextApp.resources.getString(R.string.PIXABAY_API_KEY),
                                        query,
                                        "orange"
                                     //   1
                                )
                        }

                        override fun processResponse(response: ApiSuccessResponse<ImageSearchResponse>)
                                : ImageSearchResponse {
                                val p = response
                                val pp =p
                                return super.processResponse(response)
                        }

                        override fun shouldFetch(data: List<Image>?): Boolean = data == null

                        override fun loadFromDb(item: ImageSearchResponse): LiveData<List<Image>> {
                                val live = MutableLiveData<List<Image>>()
                                val list = mutableListOf<List<Image>>()
                                list.add(0, item.hits)
                                live.value = list[0]
                                return live
                        }

                }.asLiveData()
        }
}