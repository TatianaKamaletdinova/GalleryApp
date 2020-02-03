package com.kamaltatyana.redgallery.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kamaltatyana.redgallery.AppExecutors
import com.kamaltatyana.redgallery.api.ApiEmptyResponse
import com.kamaltatyana.redgallery.api.ApiErrorResponse
import com.kamaltatyana.redgallery.api.ApiResponse
import com.kamaltatyana.redgallery.api.ApiSuccessResponse
import com.kamaltatyana.redgallery.vo.ListImage
import com.kamaltatyana.redgallery.vo.Resource

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MutableLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val apiResponses = createCall()
        apiResponses.observeForever{ it ->
            when (it) {
                is ApiSuccessResponse -> {
                    appExecutors.mainThread().execute {
                        loadFromDb(processResponse(it)).observeForever{
                            setValue(Resource.success(it))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    // result.addSource(apiResponse){ data -> setValue(Resource.error(response.errorMessage)) }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        // result.addSource(loadFromDb()) { newData -> setValue(Resource.success(newData)) }
                    }
                }
            }
        }


        /* val apiResponses = createCall().observeForever{
             when (it) {
                 is ApiSuccessResponse -> {
                     appExecutors.mainThread().execute {
                         val o = processResponse(it)
                         result.addSource(loadFromDb(processResponse(it))){ data ->
                             setValue(Resource.success(data))
                         }
                     }
                 }
                 is ApiErrorResponse -> {
                     onFetchFailed()
                     // result.addSource(apiResponse){ data -> setValue(Resource.error(response.errorMessage)) }
                 }
             }
         }*/
    }


    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    @MainThread
    protected abstract fun loadFromDb(item: RequestType): LiveData<ResultType>

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    protected open fun onFetchFailed() {}

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}