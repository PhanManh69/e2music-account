package com.mobile.e2m.account.domain.repository

import com.mobile.e2m.core.datasource.local.room.entity.UsersEntity
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUsers(): Flow<List<UsersEntity>>

    fun getUsername(): Flow<List<String>>

    fun getEmail(): Flow<List<String>>

    fun getUserByEmailOrUsername(emailAccount: String): Flow<List<UsersEntity>>

    suspend fun updatePassword(id: Int, newPassword: String): Int

    suspend fun insertUser(user: UsersEntity): Long

    suspend fun updateUser(user: UsersEntity): Int

    suspend fun deleteUser(user: UsersEntity): Int
}
