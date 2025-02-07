package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToForgotPassword() {
    this.navigate(route = AccountRoute.FORGOT_PASSWORD)
}

internal fun NavGraphBuilder.forgotPasswordDestination(
    goBack: () -> Unit = { },
    goToResetPassword: (Int) -> Unit = { },
) {
    composable(AccountRoute.FORGOT_PASSWORD) {
        ForgotPasswordScreen(
            goBack = { goBack() },
            goToResetPassword = { goToResetPassword(it) },
        )
    }
}
