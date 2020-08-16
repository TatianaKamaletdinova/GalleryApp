package com.kamaltatyana.redgallery.view.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamaltatyana.redgallery.AppExecutors
import com.kamaltatyana.redgallery.R
import com.kamaltatyana.redgallery.binding.FragmentDataBindingComponent
import com.kamaltatyana.redgallery.databinding.SearchFragmentBinding
import com.kamaltatyana.redgallery.di.Injectable
import com.kamaltatyana.redgallery.ui.ImageListAdapter
import com.kamaltatyana.redgallery.util.autoCleared
import com.kamaltatyana.redgallery.view.main.MainActivity
import javax.inject.Inject

class SearchFragment : Fragment(),
    Injectable,
    SearchView.OnQueryTextListener
{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var binding by autoCleared<SearchFragmentBinding>()

    var adapter by autoCleared<ImageListAdapter>()

    private val searchViewModel: SearchViewModel by viewModels { viewModelFactory }

    private var mSearchQuery: String? = null //Строка запроса изображения
    private var searchView: SearchView? = null
    private var mHandler: Handler? = null
    private var mMenuItem: MenuItem? = null
    private var mTitleText: TextView? = null
    private var mImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_fragment,
            container,
            false,
            dataBindingComponent
        )

        mTitleText = binding.emptyTitleText
        mImage = binding.image
        val toolbar = binding.toolbar
        (context as MainActivity).setSupportActionBar(toolbar)
        mHandler = Handler()
        if (savedInstanceState != null) mSearchQuery =
            savedInstanceState.getString(SEARCH_QUERY_TAG)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        val rvAdapter = ImageListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors){}

        binding.imageList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.imageList.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()
    }

    private fun initRecyclerView() {
        binding.imageList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount-1){
                    searchViewModel.loadNextPage()
                }
            }
        })

        searchViewModel.results.observe(viewLifecycleOwner, Observer { result ->
           adapter.submitList(result?.data)
        })
    }

    fun navController() = findNavController()

    private fun initSearchInputListener() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        mMenuItem = menu.findItem(R.id.action_search)
        searchView = mMenuItem!!.getActionView() as SearchView
        searchView!!.queryHint = getString(R.string.search_text)
        searchView!!.setOnQueryTextListener(this@SearchFragment)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (mSearchQuery != null && !mSearchQuery!!.isEmpty()) {
            val query: String = mSearchQuery!!
            if (mHandler != null && searchView != null && mMenuItem != null) {
                mHandler!!.post {
                    mMenuItem!!.expandActionView()
                    searchView!!.setQuery(query, false)
                    searchView!!.clearFocus()
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mSearchQuery = query
        doSearch(query)
        return false
    }

    private fun doSearch(query: String?) {
        searchViewModel.setQuery(query)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Операции для выбранного пункта меню
        return when (item.itemId) {
            R.id.about -> {
                //showAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val o = newText
        Log.e("Log", newText)
        return false
    }

    private fun showAbout() {
        // val aboutFragment = AboutFragment()
        // aboutFragment.show(fragmentManager, "dialog")
    }

    companion object {
        /**
         * Тег для передачи строки поиска
         */
        const val SEARCH_QUERY_TAG = "search_query_tag"
        /**
         * Тег для передачи позиции элемента в списке
         */
        const val RECYCLER_LIST_STATE = "recycler_list_state"
        /**
         * Тег для передачи позиции изображения в списке
         */
        const val IMAGE_URL = "imageUrl"
    }
}