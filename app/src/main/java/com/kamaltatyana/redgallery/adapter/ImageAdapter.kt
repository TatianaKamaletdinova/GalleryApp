package com.kamaltatyana.redgallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamaltatyana.redgallery.R
import com.kamaltatyana.redgallery.adapter.ImageAdapter.MyViewHolder
import com.kamaltatyana.redgallery.pojo.Images
import java.util.*

class ImageAdapter(private val mContext: Context, private val mImagesArrayList: ArrayList<Images>) : RecyclerView.Adapter<MyViewHolder>() {
    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(mContext).inflate(R.layout.item_rv_images, parent, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val images = mImagesArrayList[position]
        val imageUrl = images.getmImageUrl()
        /**
         * Получени изображений с Glide
         */
        Glide.with(holder.itemView.context)
                .load(imageUrl)
                //.dontAnimate()
                //.error(R.drawable.images_error)
                //.centerCrop()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImageView)
    }

    override fun getItemCount(): Int {
        return mImagesArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView

        init {
            mImageView = itemView.findViewById(R.id.image_view)
            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }
                }
            }
        }
    }

}