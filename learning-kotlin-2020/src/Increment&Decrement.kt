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


/**
 * Increment => ++ => x++ means x = x + 1
 * Decrement => ++ => x-- means x = x - 1
 */
fun main() {
    var x = 12
    println("Value before change => $x")
    --x
    println("Value after --x => $x")
    x++
    println("Value after x++ => $x")
    x--
    println("Value after x-- => $x")
    ++x
    println("Value after ++x => $x")
}

