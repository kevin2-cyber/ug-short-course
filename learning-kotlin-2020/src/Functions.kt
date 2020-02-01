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
//    println(getName())
//    val x = 23
//    val y = 67
//    val result = sum(x, y, 45)
//    println("Result of adding variables $x & $y = $result")

//    createAccount(username = "Dennis", email = "g@h.com")
//    createAccount(username = "Dennis", email = "g@h.com", password = "3534535")

    // Accept input from user : readLine()

    // Ask user for a username
    print("What is your full name: ")
    val username = readLine()
    val socialName = createAwesomeUsername(username ?: "No Username")
    println("Your new handle is => $socialName")
}

//fun getName(): String {
//aakajskadjakad
//    return "Anything"
//}

fun getName(): String = "Anything"

fun sum(num1: Int, num2: Int, num3: Int = 13): Int = num1 + num2 + num3

fun createAccount(username: String?, password: String = "1234", email: String) {
    println("Your credentials are => $email & $username & $password")
}


fun createAwesomeUsername(email: String): String {
    val lowercaseFN = email
        .replace(" ", "")   // replace whitespaces
        .substring(0, email.indexOf('.'))
        .substring(0, email.indexOf('@'))
        .replace("@", "")   // replace @
        .replace(".", "")   // replace .
        .toLowerCase()  // Convert result to lowercase

    return "@$lowercaseFN"
}