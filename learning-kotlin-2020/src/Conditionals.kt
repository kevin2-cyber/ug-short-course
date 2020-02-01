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

// If
// If-Else
// If-Else-If
// When

fun main() {

    println("What was your score in Mathematics (Please be honest, no one is watching)?")
    val result = readLine()!!.toInt()

    // IF-ELSE
    /*val grade: String = if (result >= 80) {
        "Excel"
    } else if (result >= 60 && result < 80) {
         "Pass"
    } else {
        "Fail"
    }*/

    // WHEN
    /*val grade = when (result) {
        in 80..100 -> "Excel"
        in 60..79 -> "Pass"
        else -> "Fail"
    }*/

    // WHILE
    do {
        println("Passed saaaaa")
    } while (result >= 80)


//    println("Your grade for maths is = $grade")

}