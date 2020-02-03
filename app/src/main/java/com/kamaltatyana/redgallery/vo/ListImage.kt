package com.kamaltatyana.redgallery.vo

data class ListImage(
    val totalHits : Int,
    val hits : List<Image>,
    val total : Int
)