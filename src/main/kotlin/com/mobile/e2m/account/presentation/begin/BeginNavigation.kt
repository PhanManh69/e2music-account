package com.mobile.e2m.account.presentation.begin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToBegin() {
    this.navigate(route = AccountRoute.BEGIN)
}

internal fun NavGraphBuilder.beginDestination(
    goToStarted: () -> Unit = { }
) {
    composable(AccountRoute.BEGIN) {
        BeginScreen(
            goToStarted = { goToStarted() }
        )
    }
}
