package com.mobile.e2m.account.data.repository

import com.mobile.e2m.account.domain.repository.UsersRepository
import com.mobile.e2m.core.datasource.local.room.UsersLocalDataSource
import com.mobile.e2m.core.datasource.local.room.entity.UsersEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UsersRepositoryImpl(
    private val usersLocalDataSource: UsersLocalDataSource,
) : UsersRepository {
    override fun getUsers(): Flow<List<UsersEntity>> = flow {
        val usersList = usersLocalDataSource.getUsers()
        emit(usersList)
    }

    override suspend fun insertUser(user: UsersEntity): Long = usersLocalDataSource.insertUsers(user)

    override suspend fun updateUser(user: UsersEntity): Int = usersLocalDataSource.updateUsers(user)

    override suspend fun deleteUser(user: UsersEntity): Int = usersLocalDataSource.deleteUsers(user)
}
