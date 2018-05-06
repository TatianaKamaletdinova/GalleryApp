package com.kamaltatyana.yandextestapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kamaltatyana.yandextestapp.adapter.ImageAdapter;
import com.kamaltatyana.yandextestapp.pojo.Images;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    /**
     * Тег для передачи строки поиска
     */
    public final static String SEARCH_QUERY_TAG = "search_query_tag";

    /**
     *  Тег для передачи позиции элемента в списке
     */
    public final static String RECYCLER_LIST_STATE = "recycler_list_state";

    /**
     *  Тег для передачи позиции изображения в списке
     */
    public static final String IMAGE_URL = "imageUrl";

    Parcelable listState;

    /**
     *  Строка запроса изображения
     */
    private String mSearchQuery = null;

    /**
     *  Список изображений
     */
    private ArrayList<Images> mImagesArrayList;

    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    private ImageAdapter mImageAdapter;

    /**
     * Сетевой запрос
     */
    private RequestQueue mRequestQueue;

    /**
     *  Для отображения сообщений при работе приложения
     */
    private View views;
    /**
     *  Поле для поиска
     */
    private SearchView searchView;

    private Handler mHandler;

    private MenuItem mMenuItem;

    private JsonObjectRequest request;

    /**
     * Заголовок-уведомление в главном экране
     */
    private TextView mTitleText;

    private ImageView mImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Поиск элементов для отображения сообщений при работе приложения
         */
        views = findViewById(R.id.info_view);
        mTitleText = findViewById(R.id.empty_title_text);
        mImage = findViewById(R.id.image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        /**
         * Воостановление поля поиска при смене ориентации экрана
         */
        if(savedInstanceState != null) mSearchQuery = savedInstanceState.getString(SEARCH_QUERY_TAG);

        /**
         * Поиск элементов для отображения изображений в списке RecyclerView
         */
        mImagesArrayList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rv_images);
        mImageAdapter = new ImageAdapter(MainActivity.this, mImagesArrayList);
        mRecyclerView.setHasFixedSize(true);

        /**
         * Разделение списка на две колонки
         */
        mLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mImageAdapter);
    }


    /**
     * Создание поля поиска SearchView
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) mMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_text));
        searchView.setOnQueryTextListener(MainActivity.this);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        /**
         * Обработка дейстий при нажатии на кнопку поиска
         */
        mSearchQuery = query;
        parseJSON(query, null);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newquery) {

        /**
         * Отмена запросов при вводе в поле поиска текста
         */
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(request);
        }
        return false;
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /**
         * Сохранение положения прокрутки списка изображений при изменении ориентации устройства
         */
        outState.putParcelable(RECYCLER_LIST_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());

        /**
         * Сохранение текущей строки запроса в поле поиска при изменении ориентации устройства
         */
        outState.putString(SEARCH_QUERY_TAG, mSearchQuery);
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /**
         * Восстановление текущей строки запроса в поле поиска при измененной ориентации устройства
         */
        if (savedInstanceState.getString(SEARCH_QUERY_TAG)!= null){
            parseJSON(savedInstanceState.getString(SEARCH_QUERY_TAG), savedInstanceState);
            /**
             * Восстановление положения прокрутки списка изображений при изменении ориентации устройства
             */
            if(savedInstanceState instanceof Bundle){
                listState = savedInstanceState.getParcelable(RECYCLER_LIST_STATE);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Работа с библиотекой Volley, получение изображений с API pixabay.com
     */
    public void parseJSON(String query, final Bundle savedInstanceState){

        //API pixabay.com
        final String url = "https://pixabay.com/api/?key=8874094-56d923303dede986f9bbbc2ac&q=" + query + "&image_type=all&pretty=true&per_page=100&orientation=horizontal&colors=orange";

        /**
         * Очищение списока с изображениями, если он не пуст
         * */
        if (!mImagesArrayList.isEmpty()) {
            mImagesArrayList.clear();
        }

        /**
         * Создание сетевого запроса JsonObjectRequest
         * */
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        /**
         * Создание запроса JsonObjectRequest
         * */
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            /**
                             * Получение ответа объекта JSON hits
                             * */
                            JSONArray jsonArray = response.getJSONArray("hits");

                            /**
                             *  Отображение сообщения "Ничего не нашлось"
                             */
                            if (jsonArray.length()==0){
                                views.setVisibility(View.VISIBLE);
                                mTitleText.setText(R.string.result_query);
                                mImage.setImageResource(R.drawable.images);
                            }

                            /**
                             *  Заполенение списка изображениями
                             */
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String imageUrl = hit.getString("webformatURL");
                                    mImagesArrayList.add(new Images(imageUrl));
                                }
                                views.setVisibility(View.GONE);
                                mImageAdapter = new ImageAdapter(MainActivity.this, mImagesArrayList);
                                mRecyclerView.setAdapter(mImageAdapter);
                                mImageAdapter.setOnItemClickListener(MainActivity.this);

                                /**
                                 *  Восстановление положения прокрутки списка изображений при изменении ориентации устройства, если оно было
                                 */
                                if (savedInstanceState != null) {
                                    mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,   R.string.no_results, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                /**
                 * Вывод уведомлений об ошибках
                 */
                if (volleyError instanceof NetworkError) {
                    mTitleText.setText(R.string.network_error);
                } else if (volleyError instanceof ServerError) {
                    mTitleText.setText(R.string.server_error);
                } else if (volleyError instanceof AuthFailureError) {
                    mTitleText.setText(R.string.network_error);
                } else if (volleyError instanceof ParseError) {
                    mTitleText.setText(R.string.parse_error);
                } else if (volleyError instanceof NoConnectionError) {
                    mTitleText.setText(R.string.network_error);
                } else if (volleyError instanceof TimeoutError) {
                    mTitleText.setText(R.string.timeout_error);
                }
                views.setVisibility(View.VISIBLE);
                mImagesArrayList.clear();
                mImage.setImageResource(R.drawable.images);
            }
        });

        /**
         * Добавление запроса в RequestQueue
         */
        mRequestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Операции для выбранного пункта меню
        switch (item.getItemId())
        {
            case R.id.about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Отображение диалога о приложении
     */
    private void showAbout() {
        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.show(getFragmentManager(), "dialog");
    }

    /**
     * Обновление меню со строкой поиска
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mSearchQuery != null && !mSearchQuery.isEmpty()){
            final String query = mSearchQuery;
            if(mHandler != null
                    && searchView != null
                    && mMenuItem != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMenuItem.expandActionView();
                        searchView.setQuery(query, false);
                        searchView.clearFocus();
                    }
                });
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Переход в новое окно, по нажатии на изображение в списке
     */
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, ImageActivity.class);
        /**
         * Передача позиции изображения в списке
         */
        Images clickedItem = mImagesArrayList.get(position);
        detailIntent.putExtra(IMAGE_URL, clickedItem.getmImageUrl());
        startActivity(detailIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(request);
        }
    }
}
