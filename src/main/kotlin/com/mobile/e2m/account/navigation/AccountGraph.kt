package com.mobile.e2m.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.mobile.e2m.account.presentation.begin.beginDestination
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
) {
    beginDestination(
        goToStarted = {
//            navController.navigate(AccountRoute.STARTED) {
//                popUpTo(AccountRoute.BEGIN) { inclusive = true }
//            }
        }
    )
}
