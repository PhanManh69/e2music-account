package com.mobile.e2m.account.presentation.passwordRecovery.resetPassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

internal fun NavController.goToResetPassword(userId: Int) {
    this.navigate(route = "${AccountRoute.RESET_PASSWORD}/$userId")
}

internal fun NavGraphBuilder.resetPasswordDestination(
    goBack: () -> Unit = { },
    goToLogin: () -> Unit = { },
) {
    composable(
        route = "${AccountRoute.RESET_PASSWORD}/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) {
        val userId = it.arguments?.getInt("userId") ?: -1

        ResetPasswordScreen(
            userId = userId,
            goBack = { goBack() },
            goToLogin = { goToLogin() },
        )
    }
}
