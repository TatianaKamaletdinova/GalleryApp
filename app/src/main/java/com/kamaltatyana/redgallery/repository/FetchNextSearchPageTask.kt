package com.kamaltatyana.redgallery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kamaltatyana.redgallery.api.PixabayService
import com.kamaltatyana.redgallery.vo.Resource
import java.io.IOException

class FetchNextSearchPageTask constructor(
    private val query: String,
    private val pixabayService: PixabayService
): Runnable {
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        val newValue = try{
        //    val response = pixabayService.searchImage(query)
        }
        catch (e: IOException) {
           // Resource.error(e.message!!)
        }
       // _liveData.postValue(newValue)
    }
}