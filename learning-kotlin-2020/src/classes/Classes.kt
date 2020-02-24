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

package classes

import java.util.*

fun main() {
//    val technician = Technician("Dennis")
//    if (technician.isWellPaid()) println("This guy is chopping money!!!")
//    println(technician.toString())

//    val className = ClassName(UUID.randomUUID().toString(), "Dennis")
//    println(className.id)

    val uid = uid()
    val prince = User(uid, "Prince Amaning", "prince@yahoo.com")
    val stella = User(uid, "Stella Anku", "anku@yahoo.com")

    if (prince.id == stella.id) {
        println("These users are the same")
    }
    println(prince.doSomething())
    println(stella.onUserPaid())
}

private fun uid() = UUID.randomUUID().toString()


/*
abstract class Employee {
    open val name: String? = ""
    abstract fun isWellPaid(): Boolean
}

data class Technician(override val name: String? = "") : Employee() {
    override fun isWellPaid(): Boolean = true
}*/


// METHOD A
/*
class ClassName private constructor() {
    var name = ""
    fun method() {}

    constructor(name: String): this() {
        this.name = name
    }
 }*/

// METHOD B
class ClassName constructor(val id: String, var name: String) {
    fun method() {}
}

interface Person {
    val name: String
    val email: String

    fun doSomething() {
        println("My name is ${this@Person.name}")
    }
}

interface OnPaidListener {
    fun onUserPaid()
}

abstract class HumanBeing

data class User constructor(val id: String, override var name: String, override val email: String) : Person,
    OnPaidListener, HumanBeing() {

    override fun onUserPaid() {
        println("Hooray! You have been paid some alawa!!!")
    }
}
