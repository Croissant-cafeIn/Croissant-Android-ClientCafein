package com.example.cafein.store

import android.widget.TextView

data class Store(
    // val url: String, 이미지 URL로 보여주자.
    val id: Int,
    val name: String,
    val location: String,
    val theme: String,
    val ownerId: String,
    val storeUrl: String,
    val congestion: String
)
