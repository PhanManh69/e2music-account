package com.mobile.e2m.account.model

import com.mobile.e2m.core.datasource.local.room.entity.UsersEntity

data class UsersModel(
    val id: Int,
    val username: String,
    val fullname: String,
    val email: String,
    val password: String,
)

fun UsersEntity.toUsersModel() = UsersModel(
    id = id,
    username = username,
    fullname = fullname,
    email = email,
    password = password,
)
