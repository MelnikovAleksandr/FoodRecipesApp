package ru.asmelnikov.android.foodrecipesapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        //.error(R.drawable.ic_error_image)
        .into(this)
}
