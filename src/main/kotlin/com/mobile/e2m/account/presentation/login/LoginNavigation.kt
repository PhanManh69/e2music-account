package com.mobile.e2m.account.presentation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToLogin() {
    this.navigate(route = AccountRoute.LOGIN)
}

internal fun NavGraphBuilder.loginDestination() {
    composable(AccountRoute.LOGIN) {
        LoginScreen()
    }
}
