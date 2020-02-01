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
    // Mutable variables
    var x: Int = 23
    var y: Double = 23.66

    // Immutable variable
    val z = x.plus(y)

    // Interpolation of summation
    println("E.g. $x + $y = $z")
    print("----------SIMPLE ADDITION CALCULATOR----------")

    // Title
    println("Update the value of x => ")

    // Prompt user for input
    x = readLine()?.toInt() ?: x

    println("Update the value of y => ")

    // Prompt user for input
    y = readLine()?.toDouble() ?: y

    // Output result
    print("Your new result is: $x + $y = ${x.plus(y)}")

}


