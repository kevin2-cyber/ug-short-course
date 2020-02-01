import java.util.*

/*
 * Copyright (c) 2020 UG Short course (Mobile Apps)
 * Facilitator: Dennis K. Bilson Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

fun main() {
    // This throws an error saying:
    // Cannot create an instance of an abstract class
    // BaseModel<MyAwesomeClass>()
    val name = "Mobile apps"
    val model = name.toBaseModel()

    println(model.printName())
}

/**
 * Generic interface
 */
abstract class BaseModel<T> {
    abstract fun get(): T

    abstract fun toList(vararg items: T): List<T>

    fun printName(): String = this::class.java.simpleName
}

fun <R> R.toBaseModel(): BaseModel<R> {
    return object : BaseModel<R>() {
        override fun get(): R {
            return this@toBaseModel
        }

        override fun toList(vararg items: R): List<R> {
            return emptyList()
        }
    }
}