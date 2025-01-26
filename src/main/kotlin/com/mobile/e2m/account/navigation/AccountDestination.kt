package com.mobile.e2m.account.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mobile.e2m.account.presentation.AccountScreen
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

fun NavController.goToAccount(navOptions: NavOptions? = null) {
    this.navigate(route = AccountRoute.ROOT, navOptions)
}

fun NavGraphBuilder.accountDestination() {
    composable(AccountRoute.ROOT) {
        AccountScreen()
    }
}
