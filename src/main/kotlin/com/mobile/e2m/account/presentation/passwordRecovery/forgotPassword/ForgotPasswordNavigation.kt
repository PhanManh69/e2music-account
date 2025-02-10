package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToForgotPassword() {
    this.navigate(route = AppNavigationRoute.Account.ForgotPassword)
}

internal fun NavGraphBuilder.forgotPasswordDestination(
    goBack: () -> Unit = { },
    goToResetPassword: (Int) -> Unit = { },
) {
    composable<AppNavigationRoute.Account.ForgotPassword> {
        ForgotPasswordScreen(
            goBack = { goBack() },
            goToResetPassword = { goToResetPassword(it) },
        )
    }
}
