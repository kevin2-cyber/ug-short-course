/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package dev.ugscheduler.shared.datasource.local

import androidx.room.Dao
import dev.ugscheduler.shared.data.Feedback

/**
 * DAO for feedback
 */
@Dao
interface FeedbackDao : BaseDao<Feedback>