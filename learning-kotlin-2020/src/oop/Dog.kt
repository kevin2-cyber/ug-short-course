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
 * [Dog] => a subclass of [Animal]
 * Created as a `data class` rather than the conventional `class` signature
 *
 * A data class =>
 */
data class Dog(override val name: String, override val family: String) : Animal() {

    override fun isMammal(): Boolean = true

}