package com.mobile.e2m.account.presentation.started

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToStarted(vararg popUpDestinations: AppNavigationRoute) {
    this.navigate(route = AppNavigationRoute.Account.Started) {
        popUpDestinations.forEach { destination ->
            popUpTo(destination) { inclusive = true }
        }
    }
}

internal fun NavGraphBuilder.startedDestination(
    goToLogin: () -> Unit = { },
) {
    composable<AppNavigationRoute.Account.Started> {
        StartedScreen(
            goToLogin = { goToLogin() }
        )
    }
}
