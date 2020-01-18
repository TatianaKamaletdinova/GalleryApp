package com.kamaltatyana.yandextestapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        /**
         * Получение позиции изображения в списке
         */
        val intent = intent
        val imageUrl = intent.getStringExtra(MainActivity.IMAGE_URL)
        val imageView = findViewById<View>(R.id.image_view_detail) as PhotoView
        /**
         * Закрытие активити свайпом вниз
         */
        val config = SlidrConfig.Builder()
                .position(SlidrPosition.BOTTOM)
                .build()
        Slidr.attach(this, config)
        /**
         * Получени изображений с Glide
         */
        Glide.with(this)
                .load(imageUrl) //.asBitmap()
//.error(R.drawable.images_error)
//.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView)
    }
}