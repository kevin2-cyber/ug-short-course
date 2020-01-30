/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.codelabs.githubrepo.shared.data.Repo
import io.codelabs.githubrepo.shared.datasource.local.BaseDao

/**
 * Source for calls to local datasource
 */
@Dao
interface RepoDao : BaseDao<Repo> {
    @Query("select * from repos order by id asc")
    fun getAllRepos(): MutableList<Repo>

    @Query("select * from repos where id = :id")
    fun getRepo(id: Int): Repo
}