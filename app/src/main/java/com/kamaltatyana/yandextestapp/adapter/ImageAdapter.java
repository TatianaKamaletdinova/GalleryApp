package com.kamaltatyana.yandextestapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaltatyana.yandextestapp.R;
import com.kamaltatyana.yandextestapp.pojo.Images;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private ArrayList<Images> mImagesArrayList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ImageAdapter(Context context, ArrayList<Images> imagesArrayList) {
        mContext = context;
        mImagesArrayList = imagesArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(mContext).inflate(R.layout.item_rv_images, parent, false);
        return new MyViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Images images = mImagesArrayList.get(position);

        String imageUrl = images.getmImageUrl();

        /**
         *  Получени изображений с Glide
         **/
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .dontAnimate()
                .error(R.drawable.images_error)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImageView);
    }


    @Override
    public int getItemCount() {
        return mImagesArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                       int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
