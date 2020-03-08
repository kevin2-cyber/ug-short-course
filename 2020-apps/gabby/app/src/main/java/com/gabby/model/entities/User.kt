package com.gabby.model.entities

import com.gabby.model.BaseModel

data class User(
    override val id: String,
    val email: String,
    var role: String,
    override val timestamp: Long = System.currentTimeMillis(),
    var fristName: String? = null,
    var lastName: String? = null,
    var profilePicture: String? = null,
    var lastSeen: Long = System.currentTimeMillis()
) : BaseModel