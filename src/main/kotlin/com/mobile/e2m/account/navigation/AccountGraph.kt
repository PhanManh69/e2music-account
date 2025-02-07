package com.mobile.e2m.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.mobile.e2m.account.presentation.begin.beginDestination
import com.mobile.e2m.account.presentation.login.goToLogin
import com.mobile.e2m.account.presentation.login.loginDestination
import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.forgotPasswordDestination
import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.goToForgotPassword
import com.mobile.e2m.account.presentation.passwordRecovery.resetPassword.goToResetPassword
import com.mobile.e2m.account.presentation.passwordRecovery.resetPassword.resetPasswordDestination
import com.mobile.e2m.account.presentation.registerAccount.register.goToRegister
import com.mobile.e2m.account.presentation.registerAccount.register.registerDestination
import com.mobile.e2m.account.presentation.registerAccount.registrationSuccessful.goToRegistrationSuccessful
import com.mobile.e2m.account.presentation.registerAccount.registrationSuccessful.registrationSuccessfulDestination
import com.mobile.e2m.account.presentation.started.goToStarted
import com.mobile.e2m.account.presentation.started.startedDestination
import com.mobile.e2m.core.ui.navigation.route.DestinationRoute.AccountRoute

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
) {
    beginDestination(
        goToStarted = {
            navController.goToStarted()
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
        },
        goToRegister = {
            navController.goToRegister()
        }
    )

    forgotPasswordDestination(
        goBack = {
            navController.popBackStack()
        },
        goToResetPassword = {
            navController.goToResetPassword(it)
        }
    )

    resetPasswordDestination(
        goBack = {
            navController.popBackStack()
        },
        goToLogin = {
            navController.goToLogin(
                AccountRoute.FORGOT_PASSWORD, AccountRoute.LOGIN
            )
        }
    )

    registerDestination(
        goBack = {
            navController.popBackStack()
        },
        goToRegistrationSuccess = {
            navController.goToRegistrationSuccessful(
                AccountRoute.REGISTER
            )
        }
    )

    registrationSuccessfulDestination(
        goToLogin = {
            navController.goToLogin(
                AccountRoute.REGISTRATION_SUCCESSFUL, AccountRoute.LOGIN
            )
        }
    )
}
