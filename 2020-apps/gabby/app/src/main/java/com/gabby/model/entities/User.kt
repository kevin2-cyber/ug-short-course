package com.gabby.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gabby.model.BaseModel

@Entity
data class User(
        @PrimaryKey
    override val id: String,
    val email: String,
    var role: String,
    override val timestamp: Long = System.currentTimeMillis(),
    var firstName: String? = null,
    var lastName: String? = null,
    var profilePicture: String? = null,
    var lastSeen: Long = System.currentTimeMillis()
) : BaseModel