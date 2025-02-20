package com.mobile.e2m.account.presentation.passwordRecovery.resetPasswordSuccressful

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToResetPasswordSuccessful(vararg popUpDestinations: AppNavigationRoute) {
    this.navigate(route = AppNavigationRoute.Account.ResetPasswordSuccessful) {
        popUpDestinations.forEach { destination ->
            popUpTo(destination) { inclusive = true }
        }
    }
}

internal fun NavGraphBuilder.resetPasswordSuccessfulDestination(
    goToLogin: () -> Unit = { },
) {
    composable<AppNavigationRoute.Account.ResetPasswordSuccessful> {
        ResetPasswordSuccessfulScreen(
            goToLogin = { goToLogin() }
        )
    }
}
