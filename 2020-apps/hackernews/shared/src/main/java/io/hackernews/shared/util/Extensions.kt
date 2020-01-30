package io.hackernews.shared.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun String?.toMutableListOfInt(): MutableList<Int> {
    if (this == null) return mutableListOf()
    val results = mutableListOf<Int>()
    this.forEach {
        val number = it.toInt()
        results.add(number)
    }
    return results
}

// Converts any type to a live data
fun <T> T.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    liveData.postValue(this)
    return liveData
}