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

package learning


// String
// Int
// Long
// Double
// Boolean
// Float
// Char


fun main() {

    val name = "Dennis" // String
    val isOld = true // Boolean
    val age = 23    // Int
    val weight = 56.99  // Double
    val height = 188.454f   // Float
    val initials = 's'  // Char

    // Nullable
    var username: String? = null

    username = "Bilson"

    // Elvis
    println("This value is => ${username ?: "not found"}")

}