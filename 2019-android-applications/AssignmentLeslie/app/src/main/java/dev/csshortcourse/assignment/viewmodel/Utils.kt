package dev.csshortcourse.assignment.viewmodel

// Utils
object Utils {
    const val USER_REF = "users"
}

fun debugger(msg: Any?) = println("Assignment => ${msg.toString()}")

// Callback
//typealias Callback<T> = (T) -> Unit
typealias Callback<M, R> = (M, R) -> Unit

enum class Response { ERROR, COMPLETED, LOADING }