package dev.codelabs.finalmeeting

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.codelabs.finalmeeting.data.News
import java.io.InputStreamReader

// Simple deserializer for news data model
fun Context.deserializeData(fileName: String): MutableList<News> {
    // Opens the specified file in the assets directory and converts its content to news list
    return Gson().fromJson<List<News>>(
        InputStreamReader(assets.open(fileName)),
        object : TypeToken<List<News>>() {}.type
    ).toMutableList()
}

fun debugger(msg: Any?) = println("MyDebugger => ${msg.toString()}")