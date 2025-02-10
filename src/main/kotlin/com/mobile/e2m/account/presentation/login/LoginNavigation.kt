package com.mobile.e2m.account.presentation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToLogin(vararg popUpDestinations: AppNavigationRoute) {
    this.navigate(route = AppNavigationRoute.Account.Login) {
        popUpDestinations.forEach { destination ->
            popUpTo(destination) { inclusive = true }
        }
    }
}

internal fun NavGraphBuilder.loginDestination(
    goToMain: () -> Unit = { },
    goToForgotPassword: () -> Unit = { },
    goToRegister: () -> Unit = { },
) {
    composable<AppNavigationRoute.Account.Login> {
        LoginScreen(
            goToMain = { goToMain() },
            goToForgotPassword = { goToForgotPassword() },
            goToRegister = { goToRegister() },
        )
    }
}
