package com.mobile.e2m.account.domain.usecase

import com.mobile.e2m.account.domain.repository.UsersRepository
import com.mobile.e2m.core.datasource.local.room.entity.UsersEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GetUsersUseCaseImpl(
    private val usersRepository: UsersRepository,
) : GetUsersUseCase {
    override fun invoke(): Flow<List<UsersEntity>> {
        return flow {
            delay(1000L)
            emitAll(usersRepository.getUsers())
        }
    }
}
