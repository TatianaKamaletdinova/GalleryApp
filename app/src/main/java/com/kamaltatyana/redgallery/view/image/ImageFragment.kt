package com.kamaltatyana.redgallery.view.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kamaltatyana.redgallery.R
import com.kamaltatyana.redgallery.di.Injectable

class ImageFragment : Fragment(), Injectable {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }
}
