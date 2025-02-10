package com.mobile.e2m.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobile.e2m.account.presentation.AccountScreen
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

fun NavGraphBuilder.accountDestination() {
    composable<AppNavigationRoute.Account> {
        AccountScreen()
    }
}
