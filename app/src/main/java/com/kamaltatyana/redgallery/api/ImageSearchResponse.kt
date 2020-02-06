package com.kamaltatyana.redgallery.api

import com.google.gson.annotations.SerializedName
import com.kamaltatyana.redgallery.vo.Image

class ImageSearchResponse(
    @SerializedName("totalHits")
    val totalHits : Int,
    @SerializedName("hits")
    val hits : List<Image>,
    @SerializedName("total")
    val total : Int
)
{
    var nextPage: Int? = null
}