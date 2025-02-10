package com.mobile.e2m.account.presentation.registerAccount.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToRegister() {
    this.navigate(route = AppNavigationRoute.Account.Register)
}

internal fun NavGraphBuilder.registerDestination(
    goBack: () -> Unit = { },
    goToRegistrationSuccess: () -> Unit = { },
) {
    composable<AppNavigationRoute.Account.Register> {
        RegisterScreen(
            goBack = { goBack() },
            goToRegistrationSuccess = { goToRegistrationSuccess() }
        )
    }
}
