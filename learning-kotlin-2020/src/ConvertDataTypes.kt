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
    // Variables
    val name = "Shaun Long"
    val age = 34
    val weight = "80"

    // Integer to String
    println("Integer to String => ${34.toString()}")

    // String to Integer
    println("String to Integer => ${weight.toInt()}")

    // Integer to Long integer, float or double
    println("Integer to Long => ${age.toLong()}")
    println("Integer to Float => ${age.toFloat()}")
    println("Integer to Double => ${age.toDouble()}")
}

