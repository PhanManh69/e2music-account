package com.mobile.e2m.account.presentation.passwordRecovery.resetPassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToResetPassword() {
    this.navigate(route = AccountRoute.RESET_PASSWORD)
}

internal fun NavGraphBuilder.resetPasswordDestination(
    goBack: () -> Unit = { },
    goToLogin: () -> Unit = { },
) {
    composable(AccountRoute.RESET_PASSWORD) {
        ResetPasswordScreen(
            goBack = { goBack() },
            goToLogin = { goToLogin() },
        )
    }
}
