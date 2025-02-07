package com.mobile.e2m.account.domain.usecase

import com.mobile.e2m.core.datasource.local.room.entity.UsersEntity
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    operator fun invoke(): Flow<List<UsersEntity>>
}
