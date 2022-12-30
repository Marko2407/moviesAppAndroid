package com.mvukosav.moviesapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData

fun <T> AppCompatActivity.observeOnMainThread(liveData: LiveData<T>, onEach: (data: T) -> Unit) {
    liveData.observe(this) {
        runOnUiThread {
            onEach(it)
        }
    }
}
