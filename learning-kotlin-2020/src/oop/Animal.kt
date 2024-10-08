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

package oop

/**
 * Base class for all [Animal]s
 */
abstract class Animal {
    // Attributes
    abstract val name: String
    abstract val family: String

    // Function that defines whether or not an animal is a mammal
    abstract fun isMammal(): Boolean
}