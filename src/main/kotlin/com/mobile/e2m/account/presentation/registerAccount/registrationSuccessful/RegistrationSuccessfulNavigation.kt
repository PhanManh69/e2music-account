package com.mobile.e2m.account.presentation.registerAccount.registrationSuccessful

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToRegistrationSuccessful(vararg popUpDestinations: AppNavigationRoute) {
    this.navigate(route = AppNavigationRoute.Account.RegistrationSuccessful) {
        popUpDestinations.forEach { destination ->
            popUpTo(destination) { inclusive = true }
        }
    }
}

internal fun NavGraphBuilder.registrationSuccessfulDestination(
    goToLogin: () -> Unit = { },
) {
    composable<AppNavigationRoute.Account.RegistrationSuccessful> {
        RegistrationSuccessfulScreen(
            goToLogin = { goToLogin() }
        )
    }
}
