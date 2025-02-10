package com.mobile.e2m.account.presentation.begin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToBegin() {
    this.navigate(route = AppNavigationRoute.Account.Begin)
}

internal fun NavGraphBuilder.beginDestination(
    goToStarted: () -> Unit = { }
) {
    composable<AppNavigationRoute.Account.Begin> {
        BeginScreen(
            goToStarted = { goToStarted() }
        )
    }
}
