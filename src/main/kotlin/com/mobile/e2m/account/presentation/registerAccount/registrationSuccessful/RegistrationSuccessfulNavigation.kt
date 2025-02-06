package com.mobile.e2m.account.presentation.registerAccount.registrationSuccessful

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToRegistrationSuccessful(vararg popUpDestinations: String) {
    this.navigate(route = AccountRoute.REGISTRATION_SUCCESSFUL) {
        popUpDestinations.forEach { destination ->
            popUpTo(destination) { inclusive = true }
        }
    }
}

internal fun NavGraphBuilder.registrationSuccessfulDestination(
    goToLogin: () -> Unit = { },
) {
    composable(AccountRoute.REGISTRATION_SUCCESSFUL) {
        RegistrationSuccessfulScreen(
            goToLogin = { goToLogin() }
        )
    }
}
