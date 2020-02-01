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


// Arrays
// Sets
// Lists

fun main() {

    // An array => a collection of items of the same type
    println("Arrays")
    val items = arrayOf<Any>(1, "Kwame Kyei", true, 89.99, 89.99f)
//    items.forEach { println(it) }
    println()

    println("ArrayList")
    val arrayList = arrayListOf<Any>(1, "Kwame Kyei", true, 89.99, 89.99f)
    arrayList.add(0, "New item")
//    arrayList.removeAll(arrayListOf(1, true))
    arrayList.remove(1)
//    arrayList.forEach { println(it) }

    println()

    println("Sets")
    val set = setOf<Any>(1, 2, 3, 4, 5)
    val mutableSet = mutableSetOf<Any>(1, 2, 3, 4, 5)

    println("Lists")
    val list = listOf<Any>(1, 2, 3)
    list.plus(5)
//    list.forEach{ println(it)}
    val mutableList = mutableListOf<Any>(1, 2, 3)
    mutableList.add(5)
    mutableList.forEach { println(it) }
}