package com.kamaltatyana.yandextestapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import static com.kamaltatyana.yandextestapp.MainActivity.IMAGE_URL;


public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        /**
         * Получение позиции изображения в списке
         */
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(IMAGE_URL);

        final PhotoView imageView = (PhotoView) findViewById(R.id.image_view_detail);

        /**
         * Закрытие активити свайпом вниз
         */
        SlidrConfig config = new SlidrConfig.Builder()
               .position(SlidrPosition.BOTTOM)
               .build();
        Slidr.attach(this, config);

        /**
         *  Получени изображений с Glide
         **/
        Glide.with(this)
                .load(imageUrl)
                //.asBitmap()
                //.error(R.drawable.images_error)
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }
}
