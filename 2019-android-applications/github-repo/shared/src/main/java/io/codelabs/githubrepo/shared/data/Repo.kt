/*
 * Copyright (c) 2019. Designed & developed by Quabynah Codelabs(c). For the love of Android development.
 */

package io.codelabs.githubrepo.shared.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey
    val id: Int,
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String,
//    val owner: String,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val description: String
) : Parcelable