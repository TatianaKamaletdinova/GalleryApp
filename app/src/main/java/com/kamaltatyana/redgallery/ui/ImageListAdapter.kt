package com.kamaltatyana.redgallery.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.kamaltatyana.redgallery.AppExecutors
import com.kamaltatyana.redgallery.R
import com.kamaltatyana.redgallery.databinding.ItemRvImagesBinding
import com.kamaltatyana.redgallery.ui.common.DataBoundListAdapter
import com.kamaltatyana.redgallery.vo.Image

class ImageListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val repoClickCallback: ((Image) -> Unit)?
    ) : DataBoundListAdapter<Image, ItemRvImagesBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Image>(){
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
           return true
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return true
        }
    }
)
{
    override fun createBinding(parent: ViewGroup): ItemRvImagesBinding {
        val binding = DataBindingUtil.inflate<ItemRvImagesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_rv_images,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.let {
            binding.repo.let {
                repoClickCallback?.invoke(it!!)
            }
        }
        return binding
    }

    override fun bind(binding: ItemRvImagesBinding, item: Image) {
        binding.repo = item
    }
}