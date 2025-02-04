package com.mobile.e2m.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.mobile.e2m.account.presentation.begin.beginDestination
import com.mobile.e2m.account.presentation.login.goToLogin
import com.mobile.e2m.account.presentation.login.loginDestination
import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.forgotPasswordDestination
import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.goToForgotPassword
import com.mobile.e2m.account.presentation.started.startedDestination
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
) {
    beginDestination(
        goToStarted = {
            navController.navigate(AccountRoute.STARTED) {
                popUpTo(AccountRoute.BEGIN) { inclusive = true }
            }
        }
    )

    startedDestination(
        goToLogin = {
            navController.goToLogin()
        }
    )

    loginDestination(
        goToForgotPassword = {
            navController.goToForgotPassword()
        }
    )

    forgotPasswordDestination(
        goBack = {
            navController.popBackStack()
        }
    )
}
