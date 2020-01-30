/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.datasource.local.dao

import androidx.room.Dao
import io.codelabs.githubrepo.shared.data.Owner
import io.codelabs.githubrepo.shared.datasource.local.BaseDao

/**
 * API for calls to local datasource
 */
@Dao
interface OwnerDao : BaseDao<Owner>