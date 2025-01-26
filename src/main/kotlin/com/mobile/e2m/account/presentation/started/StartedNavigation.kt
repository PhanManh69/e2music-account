package com.mobile.e2m.account.presentation.started

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToStarted() {
    this.navigate(route = AccountRoute.STARTED)
}

internal fun NavGraphBuilder.startedDestination(
    goToLogin: () -> Unit = { },
) {
    composable(AccountRoute.STARTED) {
        StartedScreen(
            goToLogin = { goToLogin() }
        )
    }
}
