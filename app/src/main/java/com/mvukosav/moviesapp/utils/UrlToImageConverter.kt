package com.mvukosav.moviesapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvukosav.moviesapp.R


fun setImage(context: Context, url: String?, imageView: ImageView){
    Glide.with(context).load(url)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.movie_img)
        .into(imageView)
}
