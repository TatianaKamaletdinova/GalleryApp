package com.kamaltatyana.yandextestapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kamaltatyana.yandextestapp.MainActivity
import com.kamaltatyana.yandextestapp.adapter.ImageAdapter
import com.kamaltatyana.yandextestapp.pojo.Images
import org.json.JSONException
import java.util.*

class MainActivity : AppCompatActivity(), ImageAdapter.OnItemClickListener, SearchView.OnQueryTextListener {
    var listState: Parcelable? = null
    /**
     * Строка запроса изображения
     */
    private var mSearchQuery: String? = null
    /**
     * Список изображений
     */
    private var mImagesArrayList: ArrayList<Images>? = null
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mImageAdapter: ImageAdapter? = null
    /**
     * Сетевой запрос
     */
    private var mRequestQueue: RequestQueue? = null
    /**
     * Для отображения сообщений при работе приложения
     */
    private var views: View? = null
    /**
     * Поле для поиска
     */
    private var searchView: SearchView? = null
    private var mHandler: Handler? = null
    private var mMenuItem: MenuItem? = null
    private var request: JsonObjectRequest? = null
    /**
     * Заголовок-уведомление в главном экране
     */
    private var mTitleText: TextView? = null
    private var mImage: ImageView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Поиск элементов для отображения сообщений при работе приложения
         */
        views = findViewById(R.id.info_view)
        mTitleText = findViewById(R.id.empty_title_text)
        mImage = findViewById(R.id.image)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mHandler = Handler()
        /**
         * Воостановление поля поиска при смене ориентации экрана
         */
        if (savedInstanceState != null) mSearchQuery = savedInstanceState.getString(SEARCH_QUERY_TAG)
        /**
         * Поиск элементов для отображения изображений в списке RecyclerView
         */
        mImagesArrayList = ArrayList()
        mRecyclerView = findViewById(R.id.rv_images)
        mImageAdapter = ImageAdapter(this@MainActivity, mImagesArrayList!!)
        mRecyclerView!!.setHasFixedSize(true)
        /**
         * Разделение списка на две колонки
         */
        mLayoutManager = GridLayoutManager(this@MainActivity, 2)
        mRecyclerView!!.setLayoutManager(mLayoutManager)
        mRecyclerView!!.setAdapter(mImageAdapter)
    }

    /**
     * Создание поля поиска SearchView
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        mMenuItem = menu.findItem(R.id.action_search)
        searchView = mMenuItem!!.getActionView() as SearchView
        searchView!!.queryHint = getString(R.string.search_text)
        searchView!!.setOnQueryTextListener(this@MainActivity)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        /**
         * Обработка дейстий при нажатии на кнопку поиска
         */
        mSearchQuery = query
        parseJSON(query, null)
        return false
    }

    override fun onQueryTextChange(newquery: String): Boolean {
        /**
         * Отмена запросов при вводе в поле поиска текста
         */
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(request)
        }
        return false
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /**
         * Сохранение положения прокрутки списка изображений при изменении ориентации устройства
         */
        outState.putParcelable(RECYCLER_LIST_STATE, mRecyclerView!!.layoutManager!!.onSaveInstanceState())
        /**
         * Сохранение текущей строки запроса в поле поиска при изменении ориентации устройства
         */
        outState.putString(SEARCH_QUERY_TAG, mSearchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        /**
         * Восстановление текущей строки запроса в поле поиска при измененной ориентации устройства
         */
        if (savedInstanceState.getString(SEARCH_QUERY_TAG) != null) {
            parseJSON(savedInstanceState.getString(SEARCH_QUERY_TAG), savedInstanceState)
            /**
             * Восстановление положения прокрутки списка изображений при изменении ориентации устройства
             */
            if (savedInstanceState is Bundle) {
                listState = savedInstanceState.getParcelable(RECYCLER_LIST_STATE)
            }
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    /**
     * Работа с библиотекой Volley, получение изображений с API pixabay.com
     */
    fun parseJSON(query: String?, savedInstanceState: Bundle?) { //API pixabay.com
        val url = "https://pixabay.com/api/?key=8874094-56d923303dede986f9bbbc2ac&q=$query&image_type=all&pretty=true&per_page=100&orientation=horizontal&colors=orange"
        /**
         * Очищение списока с изображениями, если он не пуст
         */
        if (!mImagesArrayList!!.isEmpty()) {
            mImagesArrayList!!.clear()
        }
        /**
         * Создание сетевого запроса JsonObjectRequest
         */
        mRequestQueue = Volley.newRequestQueue(this@MainActivity)
        /**
         * Создание запроса JsonObjectRequest
         */
        request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        /**
                         * Получение ответа объекта JSON hits
                         */
                        /**
                         * Получение ответа объекта JSON hits
                         */
                        val jsonArray = response.getJSONArray("hits")
                        /**
                         * Отображение сообщения "Ничего не нашлось"
                         */
                        /**
                         * Отображение сообщения "Ничего не нашлось"
                         */
                        if (jsonArray.length() == 0) {
                            views!!.visibility = View.VISIBLE
                            mTitleText!!.setText(R.string.result_query)
                            mImage!!.setImageResource(R.drawable.images)
                        } else {
                            for (i in 0 until jsonArray.length()) {
                                val hit = jsonArray.getJSONObject(i)
                                val imageUrl = hit.getString("webformatURL")
                                mImagesArrayList!!.add(Images(imageUrl))
                            }
                            views!!.visibility = View.GONE
                            mImageAdapter = ImageAdapter(this@MainActivity, mImagesArrayList!!)
                            mRecyclerView!!.adapter = mImageAdapter
                            mImageAdapter!!.setOnItemClickListener(this@MainActivity)
                            /**
                             * Восстановление положения прокрутки списка изображений при изменении ориентации устройства, если оно было
                             */
                            /**
                             * Восстановление положения прокрутки списка изображений при изменении ориентации устройства, если оно было
                             */
                            if (savedInstanceState != null) {
                                mRecyclerView!!.layoutManager!!.onRestoreInstanceState(listState)
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@MainActivity, R.string.no_results, Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { volleyError ->
            /**
             * Вывод уведомлений об ошибках
             */
            /**
             * Вывод уведомлений об ошибках
             */
            if (volleyError is NetworkError) {
                mTitleText!!.setText(R.string.network_error)
            } else if (volleyError is ServerError) {
                mTitleText!!.setText(R.string.server_error)
            } else if (volleyError is AuthFailureError) {
                mTitleText!!.setText(R.string.network_error)
            } else if (volleyError is ParseError) {
                mTitleText!!.setText(R.string.parse_error)
            } else if (volleyError is NoConnectionError) {
                mTitleText!!.setText(R.string.network_error)
            } else if (volleyError is TimeoutError) {
                mTitleText!!.setText(R.string.timeout_error)
            }
            views!!.visibility = View.VISIBLE
            mImagesArrayList!!.clear()
            mImage!!.setImageResource(R.drawable.images)
        })
        /**
         * Добавление запроса в RequestQueue
         */
        mRequestQueue!!.add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Операции для выбранного пункта меню
        return when (item.itemId) {
            R.id.about -> {
                showAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Отображение диалога о приложении
     */
    private fun showAbout() {
        val aboutFragment = AboutFragment()
        aboutFragment.show(fragmentManager, "dialog")
    }

    /**
     * Обновление меню со строкой поиска
     */
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
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

    /**
     * Переход в новое окно, по нажатии на изображение в списке
     */
    override fun onItemClick(position: Int) {
        val detailIntent = Intent(this, ImageActivity::class.java)
        /**
         * Передача позиции изображения в списке
         */
        val clickedItem = mImagesArrayList!![position]
        detailIntent.putExtra(IMAGE_URL, clickedItem.getmImageUrl())
        startActivity(detailIntent)
    }

    override fun onResume() {
        super.onResume()
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(request)
        }
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