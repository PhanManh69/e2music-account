package com.mobile.e2m.account.presentation.passwordRecovery.resetPassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute

internal fun NavController.goToResetPassword(userId: Int) {
    this.navigate(route = AppNavigationRoute.Account.ResetPassword(userId).toRoute())
}

internal fun NavGraphBuilder.resetPasswordDestination(
    goBack: () -> Unit = { },
    goToSuccessful: () -> Unit = { },
) {
    composable(
        route = AppNavigationRoute.Account.ResetPassword.ROUTE,
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) {
        val userId = it.arguments?.getInt("userId") ?: -1

        ResetPasswordScreen(
            userId = userId,
            goBack = { goBack() },
            goToSuccessful = { goToSuccessful() },
        )
    }
}
