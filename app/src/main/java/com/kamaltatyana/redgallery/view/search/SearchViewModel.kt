package com.kamaltatyana.redgallery.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kamaltatyana.redgallery.repository.ImageRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(imageRepository: ImageRepository ) : ViewModel()
{
    private val _query = MutableLiveData<String>()
    val query : LiveData<String> = _query

    fun setQuery(input: String?) {
        if (input == _query.value) {
            return
        }
        _query.value = input
    }
}