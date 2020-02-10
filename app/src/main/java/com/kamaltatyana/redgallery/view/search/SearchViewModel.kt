package com.kamaltatyana.redgallery.view.search

import androidx.lifecycle.*
import com.kamaltatyana.redgallery.repository.ImageRepository
import com.kamaltatyana.redgallery.util.AbsentLiveData
import com.kamaltatyana.redgallery.vo.Image
import com.kamaltatyana.redgallery.vo.ListImage
import com.kamaltatyana.redgallery.vo.Resource
import com.kamaltatyana.redgallery.vo.Status
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel()
{
    private val _query = MutableLiveData<String>()
    private val nextPageHandler = NextPageHandler(imageRepository)

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
                nextPageHandler.queryNextPage(it)
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

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    class NextPageHandler(private val repository: ImageRepository)
        : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }


        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query
          //  nextPageLiveData = repository.searchNextPage(query)
            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            }else{
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = null
                            )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = result.message
                            )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                isRunning = false,
                errorMessage = null
            )
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }
    }
}