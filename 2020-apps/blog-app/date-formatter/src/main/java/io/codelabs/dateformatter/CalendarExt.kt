/*
 * Copyright (c) 2020 Quabynah Codelabs LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package io.codelabs.dateformatter

import java.util.*

fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.YEAR) == other.get(Calendar.YEAR) && get(Calendar.DAY_OF_YEAR) == other.get(
        Calendar.DAY_OF_YEAR)
}

fun Calendar.isSameWeek(other: Calendar): Boolean {
    return get(Calendar.YEAR) == other.get(Calendar.YEAR) && get(Calendar.WEEK_OF_YEAR) == other.get(
        Calendar.WEEK_OF_YEAR)
}

fun Calendar.isSameYear(other: Calendar): Boolean {
    return get(Calendar.YEAR) == other.get(Calendar.YEAR)
}