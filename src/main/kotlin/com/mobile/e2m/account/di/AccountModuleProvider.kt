package com.mobile.e2m.account.di

import com.mobile.e2m.account.data.repository.UsersRepositoryImpl
import com.mobile.e2m.account.domain.repository.UsersRepository
import com.mobile.e2m.account.domain.usecase.GetUsersUseCase
import com.mobile.e2m.account.domain.usecase.GetUsersUseCaseImpl
import com.mobile.e2m.account.presentation.login.LoginViewModel
import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.ForgotPasswordViewModel
import com.mobile.e2m.account.presentation.passwordRecovery.resetPassword.ResetPasswordViewModel
import com.mobile.e2m.account.presentation.registerAccount.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val accountModule = module {
    viewModel { ForgotPasswordViewModel(get()) }

    viewModel { ResetPasswordViewModel(get()) }

    viewModel { RegisterViewModel(get()) }

    viewModel { LoginViewModel(get()) }

    single { GetUsersUseCaseImpl(get()) } bind GetUsersUseCase::class

    single { UsersRepositoryImpl(get()) } bind UsersRepository::class
}
