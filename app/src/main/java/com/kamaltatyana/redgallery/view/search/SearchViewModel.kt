package com.kamaltatyana.redgallery.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kamaltatyana.redgallery.repository.ImageRepository
import com.kamaltatyana.redgallery.util.AbsentLiveData
import com.kamaltatyana.redgallery.vo.Image
import com.kamaltatyana.redgallery.vo.ListImage
import com.kamaltatyana.redgallery.vo.Resource
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel()
{
    private val _query = MutableLiveData<String>()
   // private val nextPageHandler = NextPageHandler(imageRepository)

    val query : LiveData<String> = _query

    fun setQuery(input: String?) {
        if (input == _query.value) {
            return
        }
        _query.value = input
         val result = imageRepository.search(input!!)
    }

    fun loadNextPage() {
        _query.value?.let {
            if (it.isNotBlank()) {
               // nextPageHandler.queryNextPage(it)
            }
        }
    }

    val results: LiveData<Resource<List<Image>>> = Transformations
        .switchMap(_query) { search ->
            if (search.isNullOrBlank()) {
                AbsentLiveData.create()
            } else {
                imageRepository.search(search)
            }
        }
}