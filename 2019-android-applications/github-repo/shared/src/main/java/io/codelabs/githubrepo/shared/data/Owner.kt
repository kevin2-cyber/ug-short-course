/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * [Repo] owner
 */
@Parcelize
@Entity(tableName = "owner")
data class Owner(
    @PrimaryKey
    val id: Int,
    @SerializedName("avatar_url")
    val avatar: String?,
    val url: String,
    @SerializedName("repos_url")
    val reposUrl: String,
    val type: String
) : Parcelable {
}