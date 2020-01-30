/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.util.deserializer

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ugscheduler.shared.data.Course
import dev.ugscheduler.shared.data.Facilitator
import dev.ugscheduler.shared.util.debugger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

suspend fun getFacilitators(context: Context): MutableList<Facilitator> =
    withContext(Dispatchers.IO) {
        try {
            Gson().fromJson<List<Facilitator>>(
                InputStreamReader(context.assets.open("facilitators.json")),
                object : TypeToken<List<Facilitator>>() {}.type
            ).toMutableList()
        } catch (ex: Exception) {
            debugger(ex.localizedMessage)
            mutableListOf<Facilitator>()
        }
    }

fun getCourses(context: Context): MutableList<Course> =
    context.fromJson("courses.json")

private fun Context.fromJson(fileName: String): MutableList<Course> {
    return Gson().fromJson<List<Course>>(
        InputStreamReader(assets.open(fileName)),
        object : TypeToken<List<Course>>() {}.type
    ).toMutableList()
}