package com.mobile.e2m.account.di

import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.ForgotPasswordViewModel
import com.mobile.e2m.account.presentation.passwordRecovery.resetPassword.ResetPasswordViewModel
import com.mobile.e2m.account.presentation.registerAccount.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel {
        ForgotPasswordViewModel()
    }

    viewModel {
        ResetPasswordViewModel()
    }

    viewModel {
        RegisterViewModel()
    }
}
