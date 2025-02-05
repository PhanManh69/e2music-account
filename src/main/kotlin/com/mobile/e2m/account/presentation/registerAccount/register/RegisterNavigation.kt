package com.mobile.e2m.account.presentation.registerAccount.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToRegister() {
    this.navigate(route = AccountRoute.REGISTER)
}

internal fun NavGraphBuilder.registerDestination(
    goBack: () -> Unit = { },
) {
    composable(AccountRoute.REGISTER) {
        RegisterScreen(
            goBack = { goBack() },
        )
    }
}
